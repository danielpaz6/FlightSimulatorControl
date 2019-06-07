package view;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.Model;
import model.SimModel;
import viewmodel.ViewModel;

import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;

import command.AssignmentCommand;
import command.BindCommand;
import command.Command;
import command.ConnectCommand;
import command.DefineVarCommand;
import command.DisconnectCommand;
import command.IfCommand;
import command.LoopCommand;
import command.OpenServerCommand;
import command.PrintCommand;
import command.ReturnCommand;
import command.SleepCommand;
import interpreter.CommandFactory;
import interpreter.Server;

public class Main extends Application {

    private static final String[] KEYWORDS = new String[] {
            "abstract", "assert", "boolean", "break", "byte",
            "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else",
            "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import",
            "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super",
            "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
            + "|(?<PAREN>" + PAREN_PATTERN + ")"
            + "|(?<BRACE>" + BRACE_PATTERN + ")"
            + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
            + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
            + "|(?<STRING>" + STRING_PATTERN + ")"
            + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );

    private static final String sampleCode = String.join("\n", new String[] {
        ""
    });
    
	@Override
	public void start(Stage primaryStage) {
		try {
	        CodeArea codeArea = new CodeArea();

	        // add line numbers to the left of area
	        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));

	        // recompute the syntax highlighting 500 ms after user stops editing area
	        Subscription cleanupWhenNoLongerNeedIt = codeArea

	                // plain changes = ignore style changes that are emitted when syntax highlighting is reapplied
	                // multi plain changes = save computation by not rerunning the code multiple times
	                //   when making multiple changes (e.g. renaming a method at multiple parts in file)
	                .multiPlainChanges()

	                // do not emit an event until 500 ms have passed since the last emission of previous stream
	                .successionEnds(Duration.ofMillis(500))

	                // run the following code block when previous stream emits an event
	                .subscribe(ignore -> codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText())));

	        // when no longer need syntax highlighting and wish to clean up memory leaks
	        // run: `cleanupWhenNoLongerNeedIt.unsubscribe();`

	        codeArea.replaceText(0, 0, sampleCode);
			
	        codeArea.setId("scriptContent");
	        
	        //FXMLLoader fxl = new FXMLLoader();
	        
			FXMLLoader fxl = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
			AnchorPane root = (AnchorPane)fxl.load();
			
			VirtualizedScrollPane textedit = new VirtualizedScrollPane<>(codeArea);
			
			textedit.setMinWidth(400);
			textedit.setMinHeight(250);
			textedit.setVisible(false);
			textedit.setId("#scripttext");
			textedit.setLayoutX(270);
			textedit.setLayoutY(100);
			//textedit.setStyle("scripttext");
			root.getChildren().add(textedit);
			
			//System.out.println("test");
			
			Scene scene = new Scene(root,700,400);
			
			//getHostServices().showDocument("https://eclipse.org");
			
			// Sets the icon of the application
			Image applicationIcon = new Image(getClass().getResourceAsStream("../airplane.png"));
			primaryStage.getIcons().add(applicationIcon);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Flight Simulator Control");
			primaryStage.setMinWidth(700);
			primaryStage.setMinHeight(450);
			primaryStage.setScene(scene);
			primaryStage.show();
			
			// Initializes the Server Variable that includes the Command List and Simulator variables. 
			CommandFactory<Command> exp = new CommandFactory<>();
			Server server = new Server(exp, () -> {
				return new String[] {"simX", "simY", "simZ"};
			});
			
			exp.insertCommand("openDataServer", OpenServerCommand.class);
			exp.insertCommand("connect", ConnectCommand.class);
			exp.insertCommand("var", DefineVarCommand.class);
			exp.insertCommand("if", IfCommand.class);
			exp.insertCommand("while", LoopCommand.class);
			exp.insertCommand("sleep", SleepCommand.class);
			exp.insertCommand("print", PrintCommand.class);
			exp.insertCommand("=", AssignmentCommand.class);
			exp.insertCommand("return", ReturnCommand.class);
			exp.insertCommand("disconnect", DisconnectCommand.class);
			exp.insertCommand("bind", BindCommand.class);
			
			Model m = new Model(server);
			
			ViewModel vm = new ViewModel(m); // View Model
			m.addObserver(vm);
			
			MainWindowController mwc= fxl.getController();
			mwc.setViewModel(vm);
			vm.addObserver(mwc);
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                    matcher.group("PAREN") != null ? "paren" :
                    matcher.group("BRACE") != null ? "brace" :
                    matcher.group("BRACKET") != null ? "bracket" :
                    matcher.group("SEMICOLON") != null ? "semicolon" :
                    matcher.group("STRING") != null ? "string" :
                    matcher.group("COMMENT") != null ? "comment" :
                    null; /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
}
