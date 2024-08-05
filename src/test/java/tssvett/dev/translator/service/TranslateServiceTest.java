package tssvett.dev.translator.service;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tssvett.dev.translator.dto.TranslateDto;
import tssvett.dev.translator.dto.TranslateRequestDto;
import tssvett.dev.translator.dto.TranslatedString;
import tssvett.dev.translator.entity.Translation;
import tssvett.dev.translator.integration.Impl.YandexServiceClient;
import tssvett.dev.translator.properties.YandexProperties; // Import your YandexProperties class
import tssvett.dev.translator.repository.TranslationRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TranslateServiceTest {

    @InjectMocks
    private TranslateService translateService;

    @Mock
    private YandexServiceClient yandexServiceClient;

    @Mock
    private TranslationRepository translationRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private YandexProperties yandexProperties;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(yandexServiceClient.getYandexProperties()).thenReturn(yandexProperties);
        when(yandexProperties.getMaxRequestsPerSecond()).thenReturn(10); // Return a valid value
    }

    @Test
    void testTranslateInParallelContainsBothTranslations() {
        // Arrange
        TranslateRequestDto requestDto = TranslateRequestDto.builder()
                .textToTranslate("hello world")
                .sourceLanguage("en")
                .targetLanguage("es")
                .build();
        mockTranslationResponses();

        // Act
        TranslateDto result = translateService.translateInParallel(requestDto);

        // Assert
        Set<String> expectedTranslations = new HashSet<>(Arrays.asList("hola", "mundo"));
        Set<String> actualTranslations = new HashSet<>(extractTranslations(result));

        assertTrue(actualTranslations.containsAll(expectedTranslations),
                "Результат должен содержать все переводы: " + expectedTranslations);
    }

    @Test
    void testTranslationRepositorySaveCalled() {
        // Arrange
        TranslateRequestDto requestDto = TranslateRequestDto.builder()
                .textToTranslate("hello world")
                .sourceLanguage("en")
                .targetLanguage("es")
                .build();
        mockTranslationResponses();

        // Act
        translateService.translateInParallel(requestDto);

        // Assert
        ArgumentCaptor<List<Translation>> captor = ArgumentCaptor.forClass(List.class);
        verify(translationRepository).saveTranslations(captor.capture());
        List<Translation> savedTranslations = captor.getValue();

        assertEquals(2, savedTranslations.size());

        Set<String> expectedTranslations = new HashSet<>(Arrays.asList(
                "hello:hola",
                "world:mundo"
        ));

        Set<String> actualTranslations = new HashSet<>();
        for (Translation translation : savedTranslations) {
            actualTranslations.add(translation.getTextToTranslate() + ":" + translation.getTranslatedText());
        }

        assertTrue(actualTranslations.containsAll(expectedTranslations),
                "Результат должен содержать все переводы: " + expectedTranslations);
    }

    private void mockTranslationResponses() {
        TranslateDto translateDto1 = TranslateDto.builder()
                .textToTranslate("hello")
                .translatedStrings(Collections.singletonList(new TranslatedString("hola")))
                .build();

        TranslateDto translateDto2 = TranslateDto.builder()
                .textToTranslate("world")
                .translatedStrings(Collections.singletonList(new TranslatedString("mundo")))
                .build();

        when(yandexServiceClient.translate(any(), any())).thenReturn(translateDto1, translateDto2);
    }

    private List<String> extractTranslations(TranslateDto result) {
        return result.getTranslatedStrings().stream()
                .map(TranslatedString::getText)
                .flatMap(text -> Arrays.stream(text.split(" ")))
                .collect(Collectors.toList());
    }
}