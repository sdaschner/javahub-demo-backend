package net.java.javahub.backend.images.control;

import net.java.javahub.backend.images.entity.ImageEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Images {

    public void addImage(final ImageEntity entity, final InputStream imageInput) {
        try (final ByteArrayOutputStream destination = new ByteArrayOutputStream()) {
            final byte[] buffer = new byte[1000];
            int length;
            while ((length = imageInput.read(buffer)) > -1) {
                destination.write(buffer, 0, length);
            }
            entity.setImageData(destination.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException("Could not store image, reason: " + e.getMessage(), e);
        }
    }

}