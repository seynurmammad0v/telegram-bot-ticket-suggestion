package az.telegram.bot.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileUtil {
    @Value("${offer.image.path}")
    String path;
    @Value("${offer.image.extension}")
    String extension;

    public InputFile byteArrToInputFile(byte[] array) {
        InputStream inputStream = new ByteArrayInputStream(array);
        InputFile inputFile = new InputFile();
        inputFile.setMedia(inputStream, "image");
        return inputFile;
    }

    public InputFile pathToInputFile(String path)  {
        try {
            return byteArrToInputFile(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String savePhoto(byte[] array){
        String path = this.path + UUID.randomUUID() + this.extension;
        try (FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(array);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return path;
    }

    public void deleteWithPath(String path)  {
        try {
            Files.deleteIfExists(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
