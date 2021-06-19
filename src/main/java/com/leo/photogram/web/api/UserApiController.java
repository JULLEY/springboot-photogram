package com.leo.photogram.web.api;

import com.leo.photogram.config.auth.PrincipalDetails;
import com.leo.photogram.domain.user.User;
import com.leo.photogram.handler.ex.CustomValidationApiException;
import com.leo.photogram.handler.ex.CustomValidationException;
import com.leo.photogram.service.UserService;
import com.leo.photogram.web.dto.CommRespDto;
import com.leo.photogram.web.dto.user.UserUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

    @PutMapping("/api/user/{id}")
    public CommRespDto<?> update(
            @PathVariable int id,
            @Valid UserUpdateDto userUpdateDto,
            BindingResult bindingResult,
            @AuthenticationPrincipal PrincipalDetails principalDetails){

        if(bindingResult.hasErrors()){
            Map<String, Object> errorMap = new HashMap<>();

            for(FieldError error : bindingResult.getFieldErrors()){
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            throw new CustomValidationApiException("유효성 검사 실패", errorMap);
        }else{
            User userEntity = userService.updateUserInfo(id, userUpdateDto.toEntity());
            principalDetails.setUser(userEntity); // 세션 정보 변경
            return new CommRespDto<>(1, "회원수정완료", userEntity); // 응답시에 userEntity의 모든 getter 함수가 호출되고 JSON으로 파싱하여 응답한다.
        }
    }
}