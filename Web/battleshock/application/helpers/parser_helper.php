<?php

class Parser_helper{

	private $lang;
	private $text_file;
	
	public function __construct() {
		$this->lang = substr($_SERVER['HTTP_ACCEPT_LANGUAGE'], 0, 2);
		$this->text_file = "text_".$this->lang.".php";
	}

	public function get_language()
	{
		return $this->lang;
	}

	public function get_text_file()
	{
		return $this->text_file;
	}

	public function get_text_array($page)
	{
		require_once ROOT_DIR."/application/helpers/texts/".$this->text_file;

		return $text[$page];
	}
	
}

?>