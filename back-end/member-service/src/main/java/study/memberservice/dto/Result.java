package study.memberservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
