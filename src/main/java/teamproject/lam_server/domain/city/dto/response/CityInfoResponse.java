package teamproject.lam_server.domain.city.dto.response;


import lombok.Builder;
import lombok.Getter;
import teamproject.lam_server.domain.city.entity.CityIntro;

@Getter
@Builder
public class CityInfoResponse {

    private Long id;
    private String cityName;
    private String cityInfoCat;
    private String content;
    private String image;

    public static CityInfoResponse of(CityIntro cityIntro) {
        return CityInfoResponse.builder()
                .id(cityIntro.getId())
                .cityName(cityIntro.getName().getCode())
                .cityInfoCat(cityIntro.getCityInfoCat().getCode())
                .content(cityIntro.getContent())
                .image(cityIntro.getImage())
                .build();
    }
}
