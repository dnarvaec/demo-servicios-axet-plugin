package serenityrest.utils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Lee el workbook datadriven.xlsx y reconstruye los payloads anidados usados
 * por las pruebas. La hoja recaudo comparte consulta_factura y pago_factura.
 */
public final class DataDrivenExcelReader {

    private static final Path WORKBOOK_PATH = Paths.get(
        "src", "test", "resources", "datadriven", "datadriven.xlsx"
    );
    private static final DataFormatter DATA_FORMATTER = new DataFormatter(Locale.US);
    private static final Pattern PATH_SEGMENT = Pattern.compile("([^\\[\\]]+)(?:\\[(\\d+)\\])?");

    private static final Map<RequestType, Map<String, Object>> PAYLOADS = loadPayloads();

    private DataDrivenExcelReader() {}

    public static Map<String, Object> retiroPayload() {
        return payloadFor(RequestType.RETIRO);
    }

    public static Map<String, Object> depositoPayload() {
        return payloadFor(RequestType.DEPOSITO);
    }

    public static Map<String, Object> consultaFacturaPayload(String trnRqUID) {
        Map<String, Object> payload = payloadFor(RequestType.CONSULTA_FACTURA);
        Map<String, Object> objOperacion = childMap(payload, "obj_operacion");
        Map<String, Object> transaction = childMap(objOperacion, "Transaction");
        transaction.put("TrnRqUID", trnRqUID);
        return payload;
    }

    public static Map<String, Object> pagoFacturaPayload() {
        return payloadFor(RequestType.PAGO_FACTURA);
    }

    public static Map<String, Object> pagoObligacionPayload() {
        return payloadFor(RequestType.PAGO_OBLIGACIONES);
    }

    private static Map<String, Object> payloadFor(RequestType requestType) {
        Map<String, Object> payload = PAYLOADS.get(requestType);
        if (payload == null) {
            throw new IllegalStateException("No existe payload configurado para " + requestType);
        }
        return deepCopyMap(payload);
    }

    private static Map<RequestType, Map<String, Object>> loadPayloads() {
        EnumMap<RequestType, Map<String, Object>> payloads = new EnumMap<>(RequestType.class);

        try (InputStream inputStream = Files.newInputStream(WORKBOOK_PATH);
             Workbook workbook = WorkbookFactory.create(inputStream)) {
            for (RequestType requestType : RequestType.values()) {
                payloads.put(requestType, readPayload(workbook, requestType));
            }
            return payloads;
        } catch (IOException exception) {
            throw new IllegalStateException(
                "No fue posible leer el workbook datadriven: " + WORKBOOK_PATH.toAbsolutePath(),
                exception
            );
        }
    }

    private static Map<String, Object> readPayload(Workbook workbook, RequestType requestType) {
        Sheet sheet = workbook.getSheet(requestType.sheetName);
        if (sheet == null) {
            throw new IllegalStateException("No existe la hoja '" + requestType.sheetName + "' en datadriven.xlsx");
        }

        Map<Integer, String> headers = headersOf(sheet.getRow(0));
        Row dataRow = findDataRow(sheet, headers, requestType);
        Map<String, Object> flattened = flattenedValues(headers, dataRow, requestType);

        if (flattened.isEmpty()) {
            throw new IllegalStateException(
                "No se encontraron columnas de request para " + requestType + " en la hoja " + requestType.sheetName
            );
        }

        LinkedHashMap<String, Object> nested = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : flattened.entrySet()) {
            putNestedValue(nested, entry.getKey(), entry.getValue());
        }
        return nested;
    }

    private static Map<Integer, String> headersOf(Row headerRow) {
        if (headerRow == null) {
            throw new IllegalStateException("El workbook datadriven.xlsx no tiene fila de encabezados");
        }

        LinkedHashMap<Integer, String> headers = new LinkedHashMap<>();
        for (int columnIndex = 0; columnIndex < headerRow.getLastCellNum(); columnIndex++) {
            String header = cellText(headerRow.getCell(columnIndex));
            if (!header.isBlank()) {
                headers.put(columnIndex, header);
            }
        }
        return headers;
    }

    private static Row findDataRow(Sheet sheet, Map<Integer, String> headers, RequestType requestType) {
        Integer selectorColumn = selectorColumn(headers);

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }

            if (requestType.rowSelector == null) {
                if (hasAnyPayloadValue(row, headers, null)) {
                    return row;
                }
                continue;
            }

            if (selectorColumn == null) {
                throw new IllegalStateException(
                    "La hoja '" + requestType.sheetName + "' requiere una columna tipo_request"
                );
            }

            String selectorValue = cellText(row.getCell(selectorColumn));
            if (requestType.rowSelector.equalsIgnoreCase(selectorValue)) {
                return row;
            }
        }

        throw new IllegalStateException(
            "No se encontró una fila para " + requestType + " en la hoja '" + requestType.sheetName + "'"
        );
    }

    private static Integer selectorColumn(Map<Integer, String> headers) {
        for (Map.Entry<Integer, String> header : headers.entrySet()) {
            if ("tipo_request".equalsIgnoreCase(header.getValue())) {
                return header.getKey();
            }
        }
        return null;
    }

    private static boolean hasAnyPayloadValue(Row row, Map<Integer, String> headers, String prefix) {
        for (Map.Entry<Integer, String> entry : headers.entrySet()) {
            String header = entry.getValue();
            if ("tipo_request".equalsIgnoreCase(header)) {
                continue;
            }
            if (prefix != null && !header.startsWith(prefix + ".")) {
                continue;
            }
            if (!cellText(row.getCell(entry.getKey())).isBlank()) {
                return true;
            }
        }
        return false;
    }

    private static Map<String, Object> flattenedValues(
        Map<Integer, String> headers,
        Row row,
        RequestType requestType
    ) {
        LinkedHashMap<String, Object> flattened = new LinkedHashMap<>();

        for (Map.Entry<Integer, String> entry : headers.entrySet()) {
            String header = entry.getValue();
            if ("tipo_request".equalsIgnoreCase(header)) {
                continue;
            }

            if (requestType.rowSelector != null) {
                String requiredPrefix = requestType.rowSelector + ".";
                if (!header.startsWith(requiredPrefix)) {
                    continue;
                }
                header = header.substring(requiredPrefix.length());
            }

            String cellValue = cellText(row.getCell(entry.getKey()));
            if (cellValue.isBlank()) {
                continue;
            }
            flattened.put(header, parseValue(cellValue));
        }

        return flattened;
    }

    private static String cellText(Cell cell) {
        if (cell == null) {
            return "";
        }
        return DATA_FORMATTER.formatCellValue(cell).trim();
    }

    private static Object parseValue(String rawValue) {
        if (rawValue.matches("-?\\d+\\.\\d+")) {
            return new BigDecimal(rawValue);
        }

        if (rawValue.matches("-?\\d+")) {
            if (hasLeadingZeros(rawValue)) {
                return rawValue;
            }
            try {
                return Long.parseLong(rawValue);
            } catch (NumberFormatException ignored) {
                return rawValue;
            }
        }

        return rawValue;
    }

    private static boolean hasLeadingZeros(String value) {
        if (value.startsWith("-")) {
            return value.length() > 2 && value.charAt(1) == '0';
        }
        return value.length() > 1 && value.charAt(0) == '0';
    }

    private static void putNestedValue(Map<String, Object> root, String path, Object value) {
        String[] segments = path.split("\\.");
        Object current = root;

        for (int index = 0; index < segments.length; index++) {
            Matcher matcher = PATH_SEGMENT.matcher(segments[index]);
            if (!matcher.matches()) {
                throw new IllegalStateException("Path inválido en datadriven.xlsx: " + path);
            }

            String name = matcher.group(1);
            String listIndex = matcher.group(2);
            boolean lastSegment = index == segments.length - 1;

            if (listIndex == null) {
                Map<String, Object> currentMap = asMap(current);
                if (lastSegment) {
                    currentMap.put(name, value);
                } else {
                    Object next = currentMap.get(name);
                    if (!(next instanceof Map)) {
                        next = new LinkedHashMap<String, Object>();
                        currentMap.put(name, next);
                    }
                    current = next;
                }
                continue;
            }

            Map<String, Object> currentMap = asMap(current);
            List<Object> list = asList(currentMap.get(name));
            currentMap.put(name, list);

            int position = Integer.parseInt(listIndex);
            ensureSize(list, position + 1);

            if (lastSegment) {
                list.set(position, value);
            } else {
                Object next = list.get(position);
                if (!(next instanceof Map)) {
                    next = new LinkedHashMap<String, Object>();
                    list.set(position, next);
                }
                current = next;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> asMap(Object value) {
        if (value instanceof Map<?, ?> mapValue) {
            return (Map<String, Object>) mapValue;
        }
        return new LinkedHashMap<>();
    }

    @SuppressWarnings("unchecked")
    private static List<Object> asList(Object value) {
        if (value instanceof List<?> listValue) {
            return (List<Object>) listValue;
        }
        return new ArrayList<>();
    }

    private static void ensureSize(List<Object> list, int size) {
        while (list.size() < size) {
            list.add(null);
        }
    }

    private static Map<String, Object> childMap(Map<String, Object> parent, String key) {
        Object child = parent.get(key);
        if (!(child instanceof Map<?, ?> childMap)) {
            throw new IllegalStateException("No existe el nodo esperado '" + key + "' en payload datadriven");
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>) childMap;
        return result;
    }

    private static Map<String, Object> deepCopyMap(Map<String, Object> source) {
        LinkedHashMap<String, Object> copy = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            copy.put(entry.getKey(), deepCopy(entry.getValue()));
        }
        return copy;
    }

    private static Object deepCopy(Object value) {
        if (value instanceof Map<?, ?> mapValue) {
            LinkedHashMap<String, Object> copy = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : mapValue.entrySet()) {
                copy.put(String.valueOf(entry.getKey()), deepCopy(entry.getValue()));
            }
            return copy;
        }

        if (value instanceof List<?> listValue) {
            List<Object> copy = new ArrayList<>();
            for (Object item : listValue) {
                copy.add(deepCopy(item));
            }
            return copy;
        }

        return value;
    }

    private enum RequestType {
        RETIRO("retiro", null),
        DEPOSITO("deposito", null),
        CONSULTA_FACTURA("recaudo", "consulta_factura"),
        PAGO_FACTURA("recaudo", "pago_factura"),
        PAGO_OBLIGACIONES("pago_obligaciones", null);

        private final String sheetName;
        private final String rowSelector;

        RequestType(String sheetName, String rowSelector) {
            this.sheetName = sheetName;
            this.rowSelector = rowSelector;
        }
    }
}