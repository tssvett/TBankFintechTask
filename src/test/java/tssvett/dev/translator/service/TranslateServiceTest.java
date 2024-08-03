package tssvett.dev.translator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tssvett.dev.translator.dto.TranslateDto;
import tssvett.dev.translator.dto.TranslateRequestDto;
import tssvett.dev.translator.dto.TranslatedString;
import tssvett.dev.translator.handler.exception.TooManyRequestsException;
import tssvett.dev.translator.integration.Impl.YandexServiceClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TranslateServiceTest {

    @Mock
    private YandexServiceClient yandexServiceClient;

    private TranslateService translateService;

    @BeforeEach
    void setUp() {
        translateService = new TranslateService(yandexServiceClient);
    }

    @Test
    void testTranslateInParallel() {
        // Arrange
        TranslateRequestDto translateRequestDto = new TranslateRequestDto("Hello", "en", "ru");
        TranslateDto translatedDto = TranslateDto.builder()
                .translatedStrings(List.of(new TranslatedString("Привет")))
                .build();

        when(yandexServiceClient.translate(any(TranslateRequestDto.class))).thenReturn(translatedDto);

        // Act
        TranslateDto result = translateService.translateInParallel(translateRequestDto);

        // Assert
        assertEquals("Привет", result.getTranslatedStrings().get(0).getText());
        verify(yandexServiceClient, times(1)).translate(any(TranslateRequestDto.class));
    }

    @Test
    void testTranslateWithRetry() {
        // Arrange
        TranslateRequestDto translateRequestDto = new TranslateRequestDto("Hello", "en", "ru");
        TranslateDto translatedDto = TranslateDto.builder()
                .translatedStrings(List.of(new TranslatedString("Привет")))
                .build();

        when(yandexServiceClient.translate(any(TranslateRequestDto.class)))
                .thenThrow(new TooManyRequestsException("Too many requests"))
                .thenReturn(translatedDto);

        // Act
        TranslateDto result = translateService.translateInParallel(translateRequestDto);

        // Assert
        assertEquals("Привет", result.getTranslatedStrings().get(0).getText());
        verify(yandexServiceClient, times(2)).translate(any(TranslateRequestDto.class));
    }

    @Test
    void testEmptyTextTranslation() {
        // Arrange
        TranslateRequestDto translateRequestDto = new TranslateRequestDto("", "en", "ru");
        TranslateDto translatedDto = TranslateDto.builder()
                .translatedStrings(List.of(new TranslatedString("")))
                .build();

        when(yandexServiceClient.translate(any(TranslateRequestDto.class))).thenReturn(translatedDto);

        // Act
        TranslateDto result = translateService.translateInParallel(translateRequestDto);

        // Assert
        assertEquals("", result.getTranslatedStrings().get(0).getText());
        verify(yandexServiceClient, times(1)).translate(any(TranslateRequestDto.class));
    }

    @Test
    void testOtherExceptionHandling() {
        // Arrange
        TranslateRequestDto translateRequestDto = new TranslateRequestDto("Hello", "en", "ru");

        when(yandexServiceClient.translate(any(TranslateRequestDto.class)))
                .thenThrow(new RuntimeException("Some other error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> translateService.translateInParallel(translateRequestDto));
        verify(yandexServiceClient, times(1)).translate(any(TranslateRequestDto.class));
    }
}
