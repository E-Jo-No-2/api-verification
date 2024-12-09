package com.locationbase.controller;

import com.locationbase.entity.PlacesEntity;
import com.locationbase.service.PlacesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/places")
public class PlacesController {

    @Autowired
    private PlacesService placesService;

    @PostMapping("/save")
    public ResponseEntity<?> savePlace(@RequestBody PlacesEntity place) {
        try {
            // PlacesService의 결과 메시지를 받아 처리
            String result = placesService.savePlace(place);

            // 중복 데이터일 경우 HTTP 상태 코드 409(CONFLICT) 반환
            if (result.equals("Place already exists!")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"장소가 이미 존재합니다!\"}");
            }

            // 성공적으로 저장된 경우 HTTP 상태 코드 200 반환
            return ResponseEntity.ok("{\"message\":\"장소가 성공적으로 저장되었습니다!\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\":\"알 수 없는 오류가 발생했습니다.\"}");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePlace(@PathVariable int id) {
        try {
            placesService.deletePlace(id);
            return ResponseEntity.ok("{\"message\":\"장소가 성공적으로 삭제되었습니다!\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\":\"장소 삭제 중 오류가 발생했습니다.\"}");
        }
    }
}
