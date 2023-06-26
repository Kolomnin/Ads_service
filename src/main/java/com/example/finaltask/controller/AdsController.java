package com.example.finaltask.controller;

import com.example.finaltask.model.dto.AdsDTO;
import com.example.finaltask.model.dto.CreateAdsDTO;
import com.example.finaltask.model.dto.FullAdsDTO;
import com.example.finaltask.model.dto.ResponseWrapperAds;
import com.example.finaltask.model.entity.Ads;
import com.example.finaltask.service.AdsService;
import com.example.finaltask.service.AdsImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController

@RequestMapping("/ads")

public class AdsController {

    private final AdsService adsService;

    private final AdsImageService adsImageService;
    public AdsController(AdsService adsService, AdsImageService adsImageService) {
        this.adsService = adsService;
        this.adsImageService = adsImageService;
    }

    @Operation(
            operationId = "getAllAds",
            summary = "Получить все объявления",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = {
                            @Content(mediaType = "*/*", schema = @Schema(implementation = AdsDTO.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )

    /**
     * Получить все объявления
     * public ResponseEntity<List<AdsDTO>> - это тип возвращаемого значения метода. В данном случае используется
     * ResponseEntity, который представляет собой ответ на HTTP-запрос, включающий статус ответа,
     * заголовки и тело ответа. List<AdsDTO> - это тип данных, представляющий список объявлений.
     */
//    @GetMapping
//    public ResponseEntity<List<AdsDTO>> getAllAds() {
//        List<AdsDTO> list = adsService.getAllAds();
//        return new ResponseEntity<>(list,HttpStatus.OK);
//    }
    @GetMapping()
    public ResponseEntity<ResponseWrapperAds<AdsDTO>> getAllAds() {

        ResponseWrapperAds<AdsDTO> ads = new ResponseWrapperAds<>();
        ads.setCount(adsService.getAllAds().size());
        ads.setResults(adsService.getAllAds());
        return ResponseEntity.ok(ads);
    }


    @Operation(
            operationId = "addAds",
            summary = "Добавить объявление",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created", content = {
                            @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE, schema = @Schema(implementation = AdsDTO.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )

    /**
     * Добавить объявление
     * Метод addADS принимает два параметра: adsDTO, который содержит данные объявления
     * и image, который содержит файл изображения, связанный с объявлением. Оба параметра
     * помечены аннотацией @RequestParam, что означает, что они должны быть извлечены из параметров запроса.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdsDTO> addADS(@RequestPart CreateAdsDTO properties, @RequestPart MultipartFile image, Authentication authentication) throws IOException {

        adsImageService.saveImage(adsService.addAds2(properties,authentication),image,authentication);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            operationId = "getADS",
            summary = "Получить информацию об объявлении",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = {
                            @Content(mediaType = "*/*", schema = @Schema(implementation = AdsDTO.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )

    /**
     * Получить информацию об объявлении
     * метод должен обрабатывать HTTP GET-запросы на указанном пути с переменной {id}.
     * {id} - это путь с переменной, которая будет заменена на фактическое значение в запросе.
     * Например, если запрос будет /ads/123, то переменная {id} будет заменена на значение 123
     * (@Parameter(description = "Id объявления") @PathVariable Integer id) - аннотация @PathVariable указывает,
     * что значение переменной {id} в пути должно быть связано с параметром id метода.
     * @Parameter(description = "Id объявления") - это аннотация, которая добавляет описание параметра id для д
     * окументации API. Integer id - это тип данных переменной id, которая будет содержать значение
     * идентификатора объявления
     */
//    @GetMapping("/{id}")
//    public ResponseEntity<FullAdsDTO> getADS(@Parameter(description = "Id объявления") @PathVariable Long id, Authentication authentication) {
//        FullAdsDTO fullAdsDTO =adsService.getFullAdsDTO(id,authentication);
//        return ResponseEntity.ok(fullAdsDTO);
//    }
    @GetMapping("/{id}")
    public ResponseEntity<FullAdsDTO> getADS(@Parameter(description = "Id объявления") @PathVariable("id") Long id, Authentication authentication) {
        FullAdsDTO fullAdsDTO =adsService.getFullAdsDTO(id,authentication);
        return ResponseEntity.ok(fullAdsDTO);
    }

    @Operation(
            operationId = "removeASD",
            summary = "Удалить объявление",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )

    /**
     * Удалить объявление
     * Обрабатывает DELETE-запрос на пути с переменной {id} и удаляет объявление по указанному идентификатору.
     * Метод возвращает ResponseEntity с кодом состояния HTTP HttpStatus.NO_CONTENT,
     * что означает успешное выполнение операции удаления без возвращаемого тела ответа.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeADS(@Parameter(description = "Id объявления") @PathVariable Integer id) {
        adsService.deleteAdsById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    /**
     * Изменение параметров объявления
     */
    @PutMapping("/edit")
    public ResponseEntity<Ads> editUser(@RequestBody Ads ads ) {
        adsService.editAds(ads);
        return ResponseEntity.ok(ads);
    }



    @Operation(
            operationId = "updateADS",
            summary = "Обновить информацию об объявлении",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = {
                            @Content(mediaType = "*/*", schema = @Schema(implementation = AdsDTO.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )

    /**
     * Обновить информацию об объявлении
     *@RequestBody AdsDTO adsDTO - аннотация @RequestBody указывает, что данные запроса должны быть преобразованы
     * в объект AdsDTO, который содержит обновленную информацию об объявлении. Входные данные запроса будут
     * автоматически преобразованы в объект AdsDTO на основе содержимого тела запроса.
     *
     * @PathVariable Integer id - аннотация @PathVariable указывает, что значение переменной {id} в пути должно
     * быть связано с параметром id метода. Integer id - это тип данных переменной id, которая будет содержать
     * значение идентификатора объявления.
     *
     * В данном случае, возвращается пустое тело ответа (ResponseEntity.ok().build()
     */
    @PatchMapping("/{id}")
    public ResponseEntity<AdsDTO> updateADS(@RequestBody AdsDTO adsDTO, @PathVariable Integer id) {
        return ResponseEntity.ok().build();
    }

    @Operation(
            operationId = "getAdsMe",
            summary = "Получить объявления авторизованного пользователя",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = {
                            @Content(mediaType = "*/*", schema = @Schema(implementation = AdsDTO.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )

    /**
     * Получить объявления авторизованного пользователя
     * Обрабатывает GET-запрос на пути "/ads/me" и возвращает информацию об объявлении пользователя.
     */
//    @GetMapping("/me")
//    public ResponseEntity<List<AdsDTO>> getADSMe() {
//
//        List list = adsService.getAllAds();
//        return new ResponseEntity<List<AdsDTO>>(list,HttpStatus.OK);
//    }
//    @GetMapping("/me")
//    public ResponseEntity<FullAdsDTO> getADSMe(Authentication authentication) {
//        FullAdsDTO fullAdsDTO = adsService.getFullAdsDTO(authentication);
//
//        return new ResponseEntity(fullAdsDTO,HttpStatus.OK);
//    }
    @GetMapping("/me")
    public ResponseEntity<ResponseWrapperAds<AdsDTO>> getAllAds2() {

        ResponseWrapperAds<AdsDTO> ads = new ResponseWrapperAds<>();
        ads.setCount(adsService.getAllAds().size());
        ads.setResults(adsService.getAllAds());
        return ResponseEntity.ok(ads);
    }


    @Operation(
            operationId = "updateImage",
            summary = "Обновить картинку объявления",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = {
                            @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE, schema = @Schema(implementation = AdsDTO.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )

    /**
     * Обновить картинку объявления
     *
     * value = "/{id}/image" - это путь, по которому будет доступен данный метод.
     *
     * consumes = MediaType.MULTIPART_FORM_DATA_VALUE - указывает, что данный метод ожидает получить
     * данные в формате multipart/form-data, который часто используется для передачи файлов вместе с другими
     * данными в HTTP-запросах.
     *
     * public ResponseEntity<byte[]> - это тип возвращаемого значения метода. В данном случае
     * используется ResponseEntity, который представляет собой ответ на HTTP-запрос, включающий статус ответа,
     * заголовки и тело ответа. byte[] - это тип данных, представляющий содержимое изображения объявления.
     *
     * @PathVariable Integer id - аннотация @PathVariable указывает, что значение переменной {id} в пути должно
     * быть связано с параметром id метода. Integer id - это тип данных переменной id, которая будет содержать
     * значение идентификатора объявления.
     *
     * @RequestParam("image") MultipartFile image - аннотация @RequestParam указывает, что параметр image метода
     * должен быть связан с файлом, который будет отправлен в запросе под именем image. MultipartFile - это тип данных,
     * представляющий загружаемый файл.
     *
     * Метод возвращает ResponseEntity с кодом состояния HTTP 200 (OK), что означает успешное выполнение операции.
     * В данном случае, возвращается пустое тело ответа (ResponseEntity.ok().build())
     */
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> updateADSImage(@PathVariable Integer id, @RequestParam("image") MultipartFile image) {
        return ResponseEntity.ok().build();
    }
}
