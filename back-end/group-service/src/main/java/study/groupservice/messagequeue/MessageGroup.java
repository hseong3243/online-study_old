package study.groupservice.messagequeue;

import lombok.Data;
import study.groupservice.domain.Group;

@Data
public class MessageGroup {

    private Long groupId;
    private String name;
    private int level;

    public static MessageGroup from(Group group) {
        MessageGroup messageGroup = new MessageGroup();
        messageGroup.groupId = group.getId();
        messageGroup.name = group.getName();
        messageGroup.level = group.getLevel();
        return messageGroup;
    }
}
