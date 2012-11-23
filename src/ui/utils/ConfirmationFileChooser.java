package ui.utils;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class ConfirmationFileChooser extends JFileChooser {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConfirmationFileChooser(File file) {
		super(file);
	}
	//This code snipped was found on: http://stackoverflow.com/questions/3651494/jfilechooser-with-confirmation-dialog
    @Override
    public void approveSelection(){
        File f = getSelectedFile();
        if(f.exists() && getDialogType() == SAVE_DIALOG){
            int result = JOptionPane.showConfirmDialog(this,"File already exists. Would you like to overwrite?","Existing file",JOptionPane.YES_NO_CANCEL_OPTION);
            switch(result){
                case JOptionPane.YES_OPTION:
                    super.approveSelection();
                    return;
                case JOptionPane.NO_OPTION:
                    return;
                case JOptionPane.CLOSED_OPTION:
                    return;
                case JOptionPane.CANCEL_OPTION:
                    cancelSelection();
                    return;
            }
        }
        super.approveSelection();
    }		    
}
