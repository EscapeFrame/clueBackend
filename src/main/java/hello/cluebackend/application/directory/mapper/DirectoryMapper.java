package hello.cluebackend.application.directory.mapper;

import hello.cluebackend.application.directory.dto.DirectoryAllInfoDto;
import hello.cluebackend.application.document.dto.DocumentAllInfoDto;
import hello.cluebackend.domain.directory.model.Directory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DirectoryMapper {
    public DirectoryAllInfoDto toDirectoryAllInfoDto(Directory directory, List<DocumentAllInfoDto> documentDtoList) {
        return DirectoryAllInfoDto.builder()
                .directoryId(directory.getDirectoryId())
                .directoryName(directory.getName())
                .directoryOrder(directory.getDirectoryOrder())
                .documentList(documentDtoList)
                .build();
    }
}
