package game;

import bagel.DrawOptions;
import bagel.Font;
import bagel.util.Colour;

import java.util.Properties;

public class EndScreen {
    private final Font winFont;
    private final String winText;
    private final double winPosY;

    private final Font loseFont;
    private final String loseText;
    private final double losePosY;

    private final String[] instructionLines;
    private final double startY;
    private final double rowGap;

    // The explanatory text can be in the same font size as "win/lose".
    private final Font instructionFont;

    public EndScreen() {
        GameConfig config = GameConfig.getInstance();
        String fontPath = config.getProperty("text.font");

        int winSize = Integer.parseInt(config.getProperty("end.win.size"));
        this.winFont = new Font(fontPath, winSize);
        this.winText = config.getProperty("end.win.text");
        this.winPosY = Double.parseDouble(config.getProperty("end.win.posY"));

        int loseSize = Integer.parseInt(config.getProperty("end.lose.size"));
        this.loseFont = new Font(fontPath, loseSize);
        this.loseText = config.getProperty("end.lose.text");
        this.losePosY = Double.parseDouble(config.getProperty("end.lose.posY"));

        String raw = config.getProperty("end.instructionsList.text");
        this.instructionLines = raw.split(",");
        this.startY = Double.parseDouble(config.getProperty("end.instructionsList.startPosY"));
        this.rowGap = Double.parseDouble(config.getProperty("end.instructionsList.rowGap"));

        this.instructionFont = loseFont;
    }

    /**
     * Render the end interface
     * @param screenWidth Screen width, for centering
     * @param isWin
     * true = Display victory text, false = Display defeat text
     * @param textColour  text color
     */
    public void render(double screenWidth, boolean isWin, Colour textColour) {
        DrawOptions opt = new DrawOptions().setBlendColour(textColour);

        // Choose the corresponding text and font based on "win/lose"
        if (isWin) {
            double x = (screenWidth - winFont.getWidth(winText)) / 2.0;
            winFont.drawString(winText, x, winPosY, opt);
        } else {
            double x = (screenWidth - loseFont.getWidth(loseText)) / 2.0;
            loseFont.drawString(loseText, x, losePosY, opt);
        }

        // The description text is displayed in both states.
        for (int i = 0; i < instructionLines.length; i++) {
            String line = instructionLines[i].trim();
            double lineX = ((
                    screenWidth - instructionFont.getWidth(line)
            ) / 2.0);
            double lineY = startY + i * rowGap;
            instructionFont.drawString(line, lineX, lineY, opt);
        }
    }
}