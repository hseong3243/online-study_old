package study.studyservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketParam {

    @NotNull(message = "MemberId cannot be null.")
    private String memberId;
    @NotNull(message = "RoomId cannot be null.")
    private Long roomId;
    @NotNull(message = "Seat cannot be null.")
    private int seat;
    private Long studyId;
}
