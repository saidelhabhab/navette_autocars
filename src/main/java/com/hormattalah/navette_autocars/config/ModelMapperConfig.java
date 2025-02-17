package com.hormattalah.navette_autocars.config;

import com.hormattalah.navette_autocars.entity.*;
import com.hormattalah.navette_autocars.request.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addMappings(new PropertyMap<User, UserDto>() {

            @Override
            protected void configure() {
                map().setByteImg(source.getImg());
                skip(destination.getImg()); // Skip MultipartFile field
            }
        });

        // Existing mapping for Navette -> NavetteDto
        modelMapper.addMappings(new PropertyMap<Navette, NavetteDto>() {
            @Override
            protected void configure() {

                map().setUserId(source.getUser().getId());
                map().setFirstName(source.getUser().getFirstName());
                map().setLastName(source.getUser().getLastName());

                // Explicitly map busName using the Bus entity's getBusName() method.
                map().setBusName(source.getBus().getBusName());

                // Explicitly map toCityName from the ToCity entity's property
                map().setToCityName(source.getToCity().getToCityName());

                // Map cityName from the City entity
                map().setCityName(source.getCity().getCityName());

                // Skip mapping the MultipartFile field
                skip(destination.getImg());
            }
        });

        modelMapper.addMappings(new PropertyMap<Bus, BusDto>() {
            @Override
            protected void configure() {
                map().setUserId(source.getUser().getId());
                map().setFirstName(source.getUser().getFirstName());
                map().setLastName(source.getUser().getLastName());
                map().setByteImg(source.getImg());
                skip(destination.getImg());
            }

        });

        // New mapping for Subscription -> SubscriptionDto
        modelMapper.addMappings(new PropertyMap<Subscription, SubscriptionDto>() {
            @Override
            protected void configure() {
                // Map basic fields
                map().setDateStart(source.getDateStart());
                map().setDateEnd(source.getDateEnd());
                map().setSubscriptionDate(source.getSubscriptionDate());

                // Map User information
                map().setUserId(source.getUser().getId());
                map().setFirstName(source.getUser().getFirstName());
                map().setLastName(source.getUser().getLastName());

                // Map Navette information
                map().setNavetteId(source.getNavette().getId());
                map().setNavetteName(source.getNavette().getName());
                map().setByteImg(source.getNavette().getImg());
                skip(destination.getImg()); // Skip MultipartFile field

                // Map related City information from Navette
                map().setCityName(source.getNavette().getCity().getCityName());

                // Map related ToCity information from Navette
                map().setToCityName(source.getNavette().getToCity().getToCityName());
            }
        });


        modelMapper.addMappings(new PropertyMap<RequestNavette, RequestNavetteDto>() {
            @Override
            protected void configure() {
                map().setUserId(source.getUser().getId());
                map().setFirstName(source.getUser().getFirstName());
                map().setLastName(source.getUser().getLastName());
            }

        });

        return modelMapper;
    }
}
