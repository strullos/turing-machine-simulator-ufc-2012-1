package editor;

import ui_utils.TextEditComponent;

public abstract class ModuleTextDocument extends ModuleDocument {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected TextEditComponent m_module_input;	
	
	public ModuleTextDocument()
	{		
	}
	
	public String GetModuleText()
	{
		return m_module_input.GetText();
	}
	
	public void SetModuleText(String machine_text)
	{
		m_module_input.SetText(machine_text);
	}
	
	@Override
	protected void IncreaseFontSize()
	{
		super.IncreaseFontSize();
		m_module_input.IncreaseFontSize();
	}
	
	@Override
	protected void DecreaseFontSize()
	{
		super.DecreaseFontSize();
		m_module_input.DecreaseFontSize();
	}
	
	@Override
	public void SwitchConsoleAndInput()
	{
		if(m_switch_to_input){
			m_input_output_tabbedPane.setSelectedIndex(0);
			m_switch_to_input = false;
			m_module_input.SetFocusOnTextArea();
		}else{
			m_input_output_tabbedPane.setSelectedIndex(1);
			m_switch_to_input = true;
			m_tape_input.SetFocusOnTextField();
		}
	}
}
