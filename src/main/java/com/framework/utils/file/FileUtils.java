package com.framework.utils.file;

import com.framework.utils.logger.TestLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Класс для работы с файлами.
 */
public class FileUtils {

    static TestLogger log = new TestLogger(FileUtils.class);

    /**
     * Дождаться когда файл станет доступен и прочитать.
     *
     * @param filePath - путь к файлу.
     * @return - файл.
     */
    public static String readFileIgnoringExceptions(String filePath) {
        Path path = Paths.get(filePath);

        // Ждём пока файл появится и станет читаемым
        await()
                .atMost(30, SECONDS)
                .pollInterval(1, SECONDS)
                .ignoreExceptions() // Игнорируем все исключения
                .until(() -> Files.exists(path) && Files.isReadable(path),
                        notNullValue());

        // Читаем файл, тоже с игнорированием исключений
        return await()
                .atMost(10, SECONDS)
                .pollInterval(500, MILLISECONDS)
                .ignoreExceptions()
                .until(() -> Files.readString(path),
                        notNullValue());
    }

    /**
     * Проверяет наличие директории для логов и создает ее, если она не существует.
     * Используется для обеспечения наличия места для сохранения логов тестов.
     */
    public static void ensureLogDirectoryExists() {
        try {
            Files.createDirectories(Paths.get("target/logs"));
        } catch (IOException e) {
            log.error("Не удалось создать директорию для логов", e);
        }
    }
}

