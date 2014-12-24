<?php

class Auth_model extends Model {
	
	public function retrieve_password($credentials,$rule = 'OR')
	{
		$condition = "";
		
		foreach ($credentials as $type => $value) {
			
			if($condition != "")
				$condition .= " {$rule} ";
			
			$condition .= $type." = '".$value."'";
		}
		
		$result = $this->query("SELECT `USE_PASSWORD` FROM `lt_users` WHERE {$condition}");
		return $result;		
	}

	public function user_exists($credentials,$rule = 'OR')
	{
		$condition = "";
		
		foreach ($credentials as $type => $value) {
			
			if($condition != "")
				$condition .= " {$rule} ";
			
			$condition .= $type." = '".$value."'";
		}
		
		$result = $this->query("SELECT * FROM `lt_users` WHERE {$condition}");
		
		if(empty($result))
		{
			return FALSE;
		}else{
			return TRUE;
		}
	}

	public function register_user($values)
	{
		$columns = "";
		$valuestr = "";

		foreach ($values as $key => $value) {
			
			if($columns == "")
				$columns = "(".$key;
			else
				$columns .= ",".$key;
			
			if($valuestr == "")
				$valuestr = "('".$value."'";
			else
				$valuestr .= ",'".$value."'";
		}

		$columns .= ")";
		$valuestr .= ")";

		$query = "INSERT INTO `lt_users` ".$columns." VALUES ".$valuestr;

		$result = $this->execute($query);

		return $result;
	}

}

?>
