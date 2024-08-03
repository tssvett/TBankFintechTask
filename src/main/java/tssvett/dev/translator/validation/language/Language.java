package tssvett.dev.translator.validation.language;

import java.util.List;

public class Language {

    // Статический список доступных языков
    private static final List<String> SUPPORTED_LANGUAGES = List.of("af", "am", "ar", "az", "ba", "be", "bg",
            "bn", "bs", "ca", "ceb", "cs", "cv", "cy", "da", "de", "el", "emj", "en", "eo", "es", "et", "eu", "fa",
            "fi", "fr", "ga", "gd", "gl", "gu", "he", "hi", "hr", "ht", "hu", "hy", "id", "is", "it", "ja", "jv", "ka",
            "kazlat", "kk", "km", "kn", "ko", "ky", "la", "lb", "lo", "lt", "lv", "mg", "mhr", "mi", "mk", "ml", "mn",
            "mr", "mrj", "ms", "mt", "my", "ne", "nl", "no", "os", "pa", "pap", "pl", "pt", "pt-BR", "ro", "ru", "sah",
            "si", "sk", "sl", "sq", "sr", "sr-Latn", "su", "sv", "sw", "ta", "te", "tg", "th", "tl", "tr", "tt", "udm",
            "uk", "ur", "uz", "uzbcyr", "vi", "xh", "yi", "zh", "zu");

    // Метод для проверки, является ли язык допустимым
    public static boolean isValidLanguage(String language) {
        return SUPPORTED_LANGUAGES.contains(language);
    }

    // Метод для получения списка поддерживаемых языков
    public static List<String> getSupportedLanguages() {
        return SUPPORTED_LANGUAGES;
    }
}
