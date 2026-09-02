package game;

import bagel.DrawOptions;
import bagel.Font;
import bagel.util.Colour;

public class StartScreen {
    private final Font titleFont;
    private final String titleText;
    private final double titlePosY;

    private final String[] instructionLines;
    private final double startY;
    private final double rowGap;

    public StartScreen() {
        GameConfig config = GameConfig.getInstance();
        String fontPath = config.getProperty("text.font");

        int titleSize = Integer.parseInt(
                config.getProperty("start.title.size")
        );
        this.titleFont = new Font(fontPath, titleSize);
        this.titleText = config.getProperty("start.title.text");
        this.titlePosY = Double.parseDouble(
                config.getProperty("start.title.posY")
        );

        // The explanatory text is a comma-separated string,
        // which should be split into an array.
        String raw = config.getProperty(
                "start.instructionsList.text"
        );
        this.instructionLines = raw.split(",");

        this.startY  = Double.parseDouble(
                config.getProperty("start.instructionsList.startPosY")
        );
        this.rowGap  = Double.parseDouble(
                config.getProperty("start.instructionsList.rowGap")
        );
    }

    /**
     * Render all the text on the start interface to be horizontally centered.
     * screenWidth The screen width, used to calculate the center position
     * textColour
     */
    public void render(double screenWidth, Colour textColour) {
        DrawOptions opt = new DrawOptions().setBlendColour(textColour);

        // Centered title: x = screen width / 2 - text width / 2
        double titleX = (screenWidth - titleFont.getWidth(titleText)) / 2.0;
        titleFont.drawString(titleText, titleX, titlePosY, opt);

        // Render the explanatory text line by line
        for (int i = 0; i < instructionLines.length; i++) {
            String line = instructionLines[i].trim();
            double lineX = (screenWidth - titleFont.getWidth(line)) / 2.0;
            double lineY = startY + i * rowGap;
            titleFont.drawString(line, lineX, lineY, opt);
        }
    }
}