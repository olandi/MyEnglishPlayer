package myPlayer.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

public class SubtitleNavigation {
    private Long[] startPoints;

    public SubtitleNavigation(String path) {
        this.startPoints = getStartTimeOfSubtitles(path);
    }

    public long getRepeatingSubtitle(long value) {
        int index = searchTheNearestStartPosition(value);
        return index == -1 ? -1 : startPoints[index];
    }

    public long getNextSubtitle(long value) {
        int index = searchTheNearestStartPosition(value);
        if (index == -1) return -1;
        return index != startPoints.length - 1 ? startPoints[index + 1] : startPoints[index];
    }

    public long getPreviousSubtitle(long value) {
        int index = searchTheNearestStartPosition(value);
        return index != 0 ? startPoints[index - 1] : startPoints[index];
    }

    private int searchTheNearestStartPosition(long value) {
        if (startPoints.length == 0) return -1;
        if (startPoints.length == 1) return 0;

        //реализация бинарного поиска
        int resultIndex = -1;
        int firstIndex = 0;
        int secondIndex = startPoints.length - 1;

        if (isBetween(value, firstIndex, secondIndex)) {
            //значение находится, когда изначальный интервал сужается до 1-го элемента (соседние индексы)
            while (secondIndex - firstIndex != 1) {

                int middle = firstIndex + (secondIndex - firstIndex) / 2;

                if (isBetween(value, firstIndex, middle)) {
                    secondIndex = middle;
                }
                if (isBetween(value, middle, secondIndex)) {
                    firstIndex = middle;
                }

            }
            resultIndex = firstIndex;

        } else if (value < startPoints[firstIndex]) {
            resultIndex = firstIndex;
        } else if (value >= startPoints[secondIndex]) {
            resultIndex = secondIndex;
        }

        return resultIndex;
    }

    private boolean isBetween(long value, int firstIndex, int secondIndex) {
        //  System.out.println(startPoints[firstIndex] + " : " + value + " : " + startPoints[secondIndex]);
        return value >= startPoints[firstIndex] && value < startPoints[secondIndex];
    }


    private Long[] getStartTimeOfSubtitles(String pathFile) {
        List<Long> longs = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(pathFile)))) {
            while (bufferedReader.ready()) {
                String a = bufferedReader.readLine();
                if (a.contains(" --> ")) {
                    String b = a.split(" --> ")[0];
                    try {
                        LocalTime localTime = LocalTime.parse(b, formatter);
                        longs.add(localTime.getLong(ChronoField.MILLI_OF_DAY));
                    } catch (DateTimeParseException e) {
                        //добавить в лог warning
                        System.out.println(e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return longs.toArray(new Long[0]);
    }

}
