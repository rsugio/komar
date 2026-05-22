import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import sun.nio.ch.IOUtil;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

class FileInfo {
    final String path, filename;
    int size;
    FileInfo parent = null;

    FileInfo(String path, String filename) {
        this.path = path;
        this.filename = filename;
    }
}

public class KovyratelTests {
    Path extractDir = Paths.get("D:\\temp");
    @Test
    public void extractDistr() throws IOException {
        List<FileInfo> result = new ArrayList<>(16384 * 256);
        Path root = Paths.get("D:\\distr\\SAP_PO\\PO75sp32");
//        root = Paths.get("D:\\workspace\\demoechoadapter\\tmp\\lib");
        DirectoryStream<Path> ds = Files.newDirectoryStream(root);

        for (Path p : ds) {
            FileInfo fi = new FileInfo(root.toString(), p.getFileName().toString());
            if (Files.isRegularFile(p)) {
                System.out.println(p);
                fi.size = Math.toIntExact(Files.size(p));
                result.add(fi);
                InputStream is = Files.newInputStream(p);
                walk(fi, is, result);
            }
//            break;
        }
    }

    void walk(FileInfo fileInfo, InputStream is, List<FileInfo> result) throws IOException {
        byte[] fullContent = IOUtils.toByteArray(is);

        // Проверяем, является ли содержимое ZIP-архивом
        boolean isZip = fullContent.length >= 4 && isZipFile(fullContent);

        if (isZip) {
            try (ByteArrayInputStream bais = new ByteArrayInputStream(fullContent);
                 ZipInputStream zis = new ZipInputStream(bais)) {

                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    if (entry.isDirectory()) {
                        continue;
                    }

                    String entryPath = fileInfo.path + "/" + fileInfo.filename;
                    String entryName = entry.getName();
                    String fileName = entryName.contains("/")
                            ? entryName.substring(entryName.lastIndexOf('/') + 1)
                            : entryName;

                    FileInfo childInfo = new FileInfo(entryPath, fileName);
                    childInfo.size = Math.toIntExact(entry.getSize());
                    childInfo.parent = fileInfo;
                    result.add(childInfo);

                    // Читаем содержимое entry
                    byte[] content =  IOUtils.toByteArray(zis);

                    // Рекурсивно обрабатываем (если внутри ещё архив)
                    try (ByteArrayInputStream childStream = new ByteArrayInputStream(content)) {
                        walk(childInfo, childStream, result);
                    }
                }
            }
        } else {
            // Если не ZIP — смотрим, интересен ли нам этот файл
            boolean interesting = fileInfo.filename.endsWith(".xml")
                    || fileInfo.filename.endsWith(".xsd")
                    || fileInfo.filename.endsWith(".xsl")
                    || fileInfo.filename.endsWith(".mmap")
                    || fileInfo.filename.endsWith(".dtd")
                    || fileInfo.filename.endsWith(".properties")
//                    || fileInfo.filename.endsWith(".txt")
//                    || fileInfo.filename.endsWith(".java")
                    ;
            boolean nope = fileInfo.filename.equals("buildinfo.xml") ||
                    fileInfo.filename.startsWith("ABAP_");

            if (interesting && !nope) {
                Path w = extractDir.resolve(fileInfo.filename);
                if (!Files.isDirectory(w)) Files.createDirectory(w);
                w = w.resolve(fileInfo.filename + "." + result.size());
                OutputStream os = Files.newOutputStream(w);
                IOUtils.write(fullContent, os);
                String x = String.format("\n<!-- from %s/%s -->", fileInfo.path, fileInfo.filename);
                IOUtils.write(x, os);
                os.close();
            }
        }
    }

    /**
     * Проверяет магические байты для ZIP-файла
     */
    private boolean isZipFile(byte[] magic) {
        // ZIP magic bytes: PK (0x50 0x4B)
        return magic.length >= 2 && magic[0] == 0x50 && magic[1] == 0x4B;
    }
}
