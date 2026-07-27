package serenityrest.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Precondicion de ejecucion:
 * 1) Lee datadriven.xlsx y cuenta las filas de datos por hoja.
 * 2) Convierte escenarios a Scenario Outline (Esquema del escenario).
 * 3) Actualiza la seccion Ejemplos de cada escenario con los casos detectados.
 */
public final class FeatureOutlinePrecondition {

    private static final Path WORKBOOK_PATH = Paths.get("src", "test", "resources", "datadriven", "datadriven.xlsx");
    private static final Path FEATURES_ROOT = Paths.get("src", "test", "resources", "features");
    private static final Path TARGET_FEATURES_ROOT = Paths.get("target", "test-classes", "features");

    private static final List<FeatureSheetBinding> BINDINGS = List.of(
        new FeatureSheetBinding(Paths.get("retiro", "retiro.feature"), "retiro"),
        new FeatureSheetBinding(Paths.get("deposito", "deposito.feature"), "deposito"),
        new FeatureSheetBinding(Paths.get("recaudo", "recaudo.feature"), "recaudo"),
        new FeatureSheetBinding(Paths.get("pagos", "pago-obligaciones.feature"), "pago_obligaciones")
    );

    private static final DataFormatter FORMATTER = new DataFormatter(Locale.US);

    private FeatureOutlinePrecondition() {
    }

    public static void main(String[] args) {
        synchronizeFeaturesWithExcel();
    }

    public static synchronized void synchronizeFeaturesWithExcel() {
        Map<String, Integer> rowsPerSheet = countRowsPerSheet();

        for (FeatureSheetBinding binding : BINDINGS) {
            Integer caseCount = rowsPerSheet.get(binding.sheetName());
            if (caseCount == null || caseCount <= 0) {
                throw new IllegalStateException("La hoja '" + binding.sheetName() + "' no tiene filas de datos para generar escenarios.");
            }

            Path featurePath = FEATURES_ROOT.resolve(binding.featurePath());
            if (!Files.exists(featurePath)) {
                throw new IllegalStateException("No existe el feature esperado: " + featurePath.toAbsolutePath());
            }

            rewriteFeatureAsOutline(featurePath, caseCount, binding.sheetName());
        }

        mirrorFeaturesToTarget();
    }

    private static Map<String, Integer> countRowsPerSheet() {
        LinkedHashMap<String, Integer> counts = new LinkedHashMap<>();

        try (InputStream inputStream = Files.newInputStream(WORKBOOK_PATH);
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            for (FeatureSheetBinding binding : BINDINGS) {
                Sheet sheet = workbook.getSheet(binding.sheetName());
                if (sheet == null) {
                    throw new IllegalStateException(
                        "No existe la hoja '" + binding.sheetName() + "' en " + WORKBOOK_PATH.toAbsolutePath()
                    );
                }
                counts.put(binding.sheetName(), countDataRows(sheet));
            }

            return counts;
        } catch (IOException exception) {
            throw new IllegalStateException(
                "No fue posible leer el archivo datadriven: " + WORKBOOK_PATH.toAbsolutePath(),
                exception
            );
        }
    }

    private static int countDataRows(Sheet sheet) {
        int count = 0;
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }
            if (hasAnyValue(row)) {
                count++;
            }
        }
        return count;
    }

    private static boolean hasAnyValue(Row row) {
        short firstCell = row.getFirstCellNum();
        short lastCell = row.getLastCellNum();
        if (firstCell < 0 || lastCell < 0) {
            return false;
        }

        for (int col = firstCell; col < lastCell; col++) {
            String value = FORMATTER.formatCellValue(row.getCell(col)).trim();
            if (!value.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private static void rewriteFeatureAsOutline(Path featurePath, int caseCount, String sheetName) {
        List<String> rawLines;
        try {
            rawLines = Files.readAllLines(featurePath, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("No fue posible leer el feature: " + featurePath.toAbsolutePath(), exception);
        }

        List<String> lines = removeAutoGeneratedExampleBlocks(rawLines);
        List<ScenarioBlock> blocks = parseScenarioBlocks(lines);
        if (blocks.isEmpty()) {
            return;
        }

        List<String> output = new ArrayList<>();
        int cursor = 0;
        for (ScenarioBlock block : blocks) {
            output.addAll(lines.subList(cursor, block.startLine()));

            if (block.isParameterizedOutline()) {
                output.addAll(lines.subList(block.startLine(), block.endLineExclusive()));
            } else {
                output.addAll(renderOutlineBlock(lines, block, caseCount, sheetName));
            }
            cursor = block.endLineExclusive();
        }

        output.addAll(lines.subList(cursor, lines.size()));
        output = normalizeScenarioSpacing(output);

        try {
            Files.write(featurePath, output, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("No fue posible actualizar el feature: " + featurePath.toAbsolutePath(), exception);
        }
    }

    private static List<String> removeAutoGeneratedExampleBlocks(List<String> lines) {
        List<String> cleaned = new ArrayList<>();

        int i = 0;
        while (i < lines.size()) {
            String trimmed = lines.get(i).trim();

            if ((trimmed.equals("Ejemplos:") || trimmed.equals("Examples:"))
                && i + 1 < lines.size()
                && lines.get(i + 1).contains("##@externaldata@src/test/resources/datadriven/datadriven.xlsx@")) {

                i += 2;
                while (i < lines.size()) {
                    String rowTrimmed = lines.get(i).trim();
                    if (rowTrimmed.isEmpty() || rowTrimmed.startsWith("|")) {
                        i++;
                        continue;
                    }
                    break;
                }
                continue;
            }

            cleaned.add(lines.get(i));
            i++;
        }

        return cleaned;
    }

    private static List<ScenarioBlock> parseScenarioBlocks(List<String> lines) {
        List<ScenarioBlock> blocks = new ArrayList<>();
        List<Integer> starts = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String trimmed = lines.get(i).trim();
            if (isScenarioLine(trimmed)) {
                starts.add(i);
            }
        }

        for (int i = 0; i < starts.size(); i++) {
            int start = starts.get(i);
            int nextStart = (i + 1 < starts.size()) ? starts.get(i + 1) : lines.size();
            int endExclusive = endBeforeNextTags(lines, start, nextStart);
            blocks.add(buildBlock(lines, start, endExclusive));
        }

        return blocks;
    }

    private static int endBeforeNextTags(List<String> lines, int currentStart, int nextStart) {
        int end = nextStart;
        while (end > currentStart + 1) {
            String prev = lines.get(end - 1).trim();
            if (prev.isEmpty() || prev.startsWith("@")) {
                end--;
                continue;
            }
            break;
        }
        return end;
    }

    private static ScenarioBlock buildBlock(List<String> lines, int start, int endExclusive) {
        boolean hasPlaceholders = false;
        int examplesStart = -1;

        for (int i = start; i < endExclusive; i++) {
            String line = lines.get(i);
            String trimmed = line.trim();

            if (examplesStart < 0 && (trimmed.startsWith("Ejemplos:") || trimmed.startsWith("Examples:"))) {
                examplesStart = i;
            }

            if (line.contains("<") && line.contains(">")) {
                hasPlaceholders = true;
            }
        }

        return new ScenarioBlock(start, endExclusive, examplesStart, hasPlaceholders);
    }

    private static boolean isScenarioLine(String trimmedLine) {
        return trimmedLine.startsWith("Escenario:") || trimmedLine.startsWith("Esquema del escenario:");
    }

    private static List<String> renderOutlineBlock(
        List<String> lines,
        ScenarioBlock block,
        int caseCount,
        String sheetName
    ) {
        List<String> rendered = new ArrayList<>();

        String scenarioLine = lines.get(block.startLine());
        rendered.add(toScenarioOutlineLine(scenarioLine));

        int contentEnd = block.examplesStartLine() >= 0 ? block.examplesStartLine() : block.endLineExclusive();
        for (int i = block.startLine() + 1; i < contentEnd; i++) {
            rendered.add(lines.get(i));
        }

        if (!rendered.isEmpty() && !rendered.get(rendered.size() - 1).trim().isEmpty()) {
            rendered.add("");
        }

        rendered.add("    Ejemplos:");
        rendered.add("      ##@externaldata@src/test/resources/datadriven/datadriven.xlsx@" + sheetName);
        rendered.add("      | Caso |");
        for (int i = 1; i <= caseCount; i++) {
            rendered.add("      |" + i + "|");
        }

        return rendered;
    }

    private static String toScenarioOutlineLine(String line) {
        if (line.contains("Escenario:")) {
            return line.replace("Escenario:", "Esquema del escenario:");
        }
        return line;
    }

    private static List<String> normalizeScenarioSpacing(List<String> lines) {
        List<String> normalized = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String current = lines.get(i);
            if (current.trim().isEmpty() && i > 0 && i + 1 < lines.size()) {
                String prev = lines.get(i - 1).trim();
                String next = lines.get(i + 1).trim();
                if (prev.startsWith("@") && isScenarioLine(next)) {
                    continue;
                }
            }
            normalized.add(current);
        }

        return normalized;
    }

    private static void mirrorFeaturesToTarget() {
        if (!Files.exists(TARGET_FEATURES_ROOT)) {
            return;
        }

        try {
            for (FeatureSheetBinding binding : BINDINGS) {
                Path source = FEATURES_ROOT.resolve(binding.featurePath());
                Path target = TARGET_FEATURES_ROOT.resolve(binding.featurePath());
                Files.createDirectories(target.getParent());
                Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("No fue posible sincronizar features en target/test-classes", exception);
        }
    }

    private record FeatureSheetBinding(Path featurePath, String sheetName) {
    }

    private record ScenarioBlock(int startLine, int endLineExclusive, int examplesStartLine, boolean isParameterizedOutline) {
    }
}
