<?php

class Main extends Controller {
	
	function index()
	{
		$parser = $this->loadHelper('Parser_helper');
		
		$text_array = $parser->get_text_array('index');

		$template = $this->loadView('main_view');

		foreach ($text_array as $text=>$value) {
			$template->set($text,$value);	
		}
		
		$template->render();
	}
}

?>
