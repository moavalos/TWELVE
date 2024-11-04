package org.twelve.presentacion.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.twelve.dominio.PaisService;
import org.twelve.presentacion.dto.PaisDTO;

@Component
public class StringToPaisDTOConverter implements Converter<String, PaisDTO> {

    private final PaisService paisService;

    public StringToPaisDTOConverter(PaisService paisService) {
        this.paisService = paisService;
    }

    @Override
    public PaisDTO convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }

        Integer paisId = Integer.valueOf(source);
        return paisService.findById(paisId);
    }
}
