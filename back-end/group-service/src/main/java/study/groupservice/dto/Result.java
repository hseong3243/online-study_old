package study.groupservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    T data;
    int totalPages;
    int number;
    boolean next;
    boolean previous;
}
