package com.locationbase.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MemoDTO {
    private int memoId;          // 메모의 고유 ID (Primary Key)
    private int plannerId;       // 관련된 플래너의 ID (Foreign Key)
    private Date writeDate;      // 메모 작성 날짜
    private String memoContent;  // 메모 내용
}
