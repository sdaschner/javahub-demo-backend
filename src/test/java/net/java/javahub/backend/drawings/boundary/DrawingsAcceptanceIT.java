package net.java.javahub.backend.drawings.boundary;

import net.java.javahub.backend.JavaHubDrawing;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.net.URI;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DrawingsAcceptanceIT {

    @Rule
    public JavaHubDrawing drawing = new JavaHubDrawing();

    @Test
    public void test() {
        final URI drawing1 = createAndRetrieveDrawing("Hello World;");
        final URI drawing2 = createAndRetrieveDrawing("Goodbye World;");
        final URI drawing3 = createAndRetrieveDrawing("Hello\nWorld;\n");
        listDrawings(drawing1, drawing2, drawing3);
    }

    private URI createAndRetrieveDrawing(final String originalSvgContent) {
        URI drawing = this.drawing.createDrawing(originalSvgContent);
        String svgContent = this.drawing.retrieveDrawing(drawing);

        assertContentsMatch(originalSvgContent, svgContent);
        return drawing;
    }

    private void listDrawings(final URI... uris) {
        final List<URI> drawings = drawing.listDrawings();
        assertThat(drawings, hasItems(uris));
    }

    private void assertContentsMatch(final String originalSvgContent, final String svgContent) {
        assertThat("SVG content paths don't match", svgContent, is(originalSvgContent));
    }

}