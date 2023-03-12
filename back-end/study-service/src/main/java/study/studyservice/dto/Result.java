package study.studyservice.dto;

import lombok.Data;

@Data
public class Result<T> {
    T data;
    private int number;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;

    public Result(T data, int number, int totalPages) {
        this.data = data;
        this.number = number;
        this.totalPages = totalPages;
    }
}
