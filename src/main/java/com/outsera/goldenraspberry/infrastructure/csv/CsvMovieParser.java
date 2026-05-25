package com.outsera.goldenraspberry.infrastructure.csv;

import com.outsera.goldenraspberry.domain.exception.CsvParsingException;
import com.outsera.goldenraspberry.domain.model.Movie;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class CsvMovieParser {

    public List<Movie> parse(InputStream inputStream) {
        try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            var format = CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .setDelimiter(CsvColumnConstants.DELIMITER)
                    .setIgnoreEmptyLines(true)
                    .setTrim(true)
                    .build();

            return format.parse(reader).getRecords().stream()
                    .filter(this::isValidRecord)
                    .map(this::toMovie)
                    .toList();

        } catch (IOException e) {
            throw new CsvParsingException("Failed to parse CSV file", e);
        }
    }

    private boolean isValidRecord(CSVRecord record) {
        return record.isSet(CsvColumnConstants.YEAR)
                && record.isSet(CsvColumnConstants.TITLE)
                && record.isSet(CsvColumnConstants.PRODUCERS)
                && !record.get(CsvColumnConstants.YEAR).isBlank()
                && !record.get(CsvColumnConstants.TITLE).isBlank()
                && !record.get(CsvColumnConstants.PRODUCERS).isBlank();
    }

    private Movie toMovie(CSVRecord record) {
        try {
            return new Movie(
                    null,
                    Integer.parseInt(record.get(CsvColumnConstants.YEAR)),
                    record.get(CsvColumnConstants.TITLE),
                    record.isSet(CsvColumnConstants.STUDIOS) ? record.get(CsvColumnConstants.STUDIOS) : "",
                    record.get(CsvColumnConstants.PRODUCERS),
                    isWinner(record)
            );
        } catch (NumberFormatException e) {
            throw new CsvParsingException("Invalid year in CSV record: " + record, e);
        }
    }

    private boolean isWinner(CSVRecord record) {
        return record.isSet(CsvColumnConstants.WINNER)
                && CsvColumnConstants.WINNER_VALUE.equalsIgnoreCase(record.get(CsvColumnConstants.WINNER));
    }
}
