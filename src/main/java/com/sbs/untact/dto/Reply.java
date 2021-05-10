package com.sbs.untact.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Reply {
    private int id;
    private String regDate;
    private String updateDate;
    private String relTypeCode;
    private int relId;
    private int parentId;
    private int memberId;
    private boolean delStatus;
    private String delDate;
    private String body;
    private boolean blindStatus;
    private String blindDate;
    private int hitCount;
    private int repliesCount;
    private int likeCount;
    private int dislikeCount;

    private String extra__writerName;

    public String getBodyForPrint() {
        String bodyForPrint = body.replaceAll("\r\n", "<br>");
        bodyForPrint = bodyForPrint.replaceAll("\r", "<br>");
        bodyForPrint = bodyForPrint.replaceAll("\n", "<br>");

        return bodyForPrint;
    }
}