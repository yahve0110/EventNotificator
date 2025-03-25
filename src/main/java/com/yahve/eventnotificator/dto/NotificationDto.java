package com.yahve.eventnotificator.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationDto {
  private Long id;
  private Long eventId;
  private FieldChangeDto name;
  private FieldChangeDto maxPlaces;
  private FieldChangeDto date;
  private FieldChangeDto cost;
  private FieldChangeDto duration;
  private FieldChangeDto locationId;
  private FieldChangeDto status;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class FieldChangeDto {
    private String oldField;
    private String newField;
  }
}
