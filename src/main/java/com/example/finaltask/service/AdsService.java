package com.example.finaltask.service;

import com.example.finaltask.mapping.AdsDtoMapper;
import com.example.finaltask.mapping.AdsMapper;
import com.example.finaltask.mapping.FullAdsMapper;
import com.example.finaltask.mapping.UserMapper;
import com.example.finaltask.model.dto.AdsDTO;
import com.example.finaltask.model.dto.CreateAdsDTO;
import com.example.finaltask.model.dto.FullAdsDTO;
import com.example.finaltask.model.dto.UserDTO;
import com.example.finaltask.model.entity.Ads;
import com.example.finaltask.repository.AdsRepository;
import com.example.finaltask.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdsService {
    private final AdsRepository adsRepository;
    private final UserRepository userRepository;

    private final UserDetailsManager manager;


    private final AdsMapper adsMapper;
    private final AdsDtoMapper adsDtoMapper;

    private final FullAdsMapper fullAdsMapper;

    private final UserMapper userMapper;


    public AdsService(AdsRepository adsRepository, UserRepository userRepository, UserDetailsManager manager, AdsMapper adsMapper, AdsDtoMapper adsDtoMapper, FullAdsMapper fullAdsMapper, UserMapper userMapper) {
        this.adsRepository = adsRepository;
        this.userRepository = userRepository;
        this.manager = manager;
        this.adsMapper = adsMapper;
        this.adsDtoMapper = adsDtoMapper;
        this.fullAdsMapper = fullAdsMapper;
        this.userMapper = userMapper;
    }

    public AdsDTO addAds1(AdsDTO properties) {
        Ads ads = adsMapper.toEntity(properties);
        AdsDTO adsDTO = adsMapper.toDto(ads);
        ads.setAuthorId(userRepository.findById(1L));//В след уроках покажут как получить
                                                        // пользователя который авторизован,пока юзер установлен
        adsRepository.save(ads);
        return adsDTO;
    }
    public Ads addAds2(CreateAdsDTO properties, Authentication authentication) {
        Ads ads = adsDtoMapper.toEntity(properties);
        System.out.println("Объявление создано");
        System.out.println(properties.getDescription());
        AdsDTO adsDTO = adsMapper.toDto(ads);
        System.out.println(adsDTO);
        ads.setAuthorId(userRepository.findByLogin(authentication.getName()));//В след уроках покажут как получить
        // пользователя который авторизован,пока юзер установлен
        adsRepository.save(ads);
        return ads;
    }

    public Ads getAdsById(Long id) {
        return adsRepository.findById(id);
    }

    public List<AdsDTO> getAllAds (){
        List<Ads> adsList = adsRepository.findAll();
        List<AdsDTO> adsDTOS = new ArrayList<>();
        for (Ads ads : adsList) {
            adsDTOS.add(adsMapper.toDto(ads));
        }
        return adsDTOS;
    }

    public void deleteAdsById(Integer id) {
        adsRepository.deleteById(id);
    }
    public Ads editAds(Ads ads ) {
        return adsRepository.save(ads);
    }

    public FullAdsDTO getFullAdsDTO(Authentication authentication) {
        List<Ads> adsList = adsRepository.findAll();
        List<AdsDTO> adsDTOS = new ArrayList<>();
        UserDTO userDTO = userMapper.toDto(userRepository.findByLogin(authentication.getName()));
//        AdsDTO adsDTO = adsMapper.toDto(adsRepository.findByAuthorId(userRepository.findByLogin(authentication.getName()).getId()));
        AdsDTO adsDTO = adsMapper.toDto(adsRepository.findByAuthorIdLogin(authentication.getName()));
        System.out.println(adsDTO);
        FullAdsDTO fullAdsDTO = fullAdsMapper.mergeAdsAndUserAndAds(userDTO,adsDTO);
        fullAdsDTO.setDescription(adsRepository.findByAuthorIdLogin(authentication.getName()).getDescription());


   return fullAdsDTO;
    }

}
