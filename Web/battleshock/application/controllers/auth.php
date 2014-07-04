<?php

class Auth extends Controller {
	
	function index()
	{
		http_response_code(400);
		die("No Event Found");
	}
    
    function recover_password()
    {
        $credentials = $_POST;
        
        $model = $this->loadModel('Auth_model');

        $user_exists = $model->user_exists($credentials,'AND');

        if(!$user_exists)
        {
            echo "Wrong Credentials";
            return;
        }

        $password = $model->retrieve_password($credentials,'AND');
        
        echo $password[0]->USE_PASSWORD;
    }

    function signup()
    {
    	$values = $_POST;
		$model = $this->loadModel('Auth_model');

        /*
        **
        **   Start: Validation
        **
        */

        if(strlen($values['USE_LOGIN'])>25)
            return "Error: Login must be 25 chars long max.";
        if(strlen($values['USE_NICKNAME'])>25)
            return "Error: Nickname must be 25 chars long max.";
        if(strlen($values['USE_NAME'])>65)
            return "Error: Name must be 65 chars long max.";
        if(strlen($values['USE_LASTNAME'])>65)
            return "Error: Lastname must be 65 chars long max.";
        if(strlen($values['USE_PASSWORD'])<8)
            return "Error: Password must be at least 8 chars long.";
        if (preg_match('/[^A-Za-z]/', $values['USE_NAME'])) 
            return "Error: Name must contain only letters and spaces.";
        if (preg_match('/[^A-Za-z]/', $values['USE_LASTNAME']))
            return "Error: Lastname must contain only letters and spaces.";
        if (preg_match('/[^A-Za-z0-9]/', $values['USE_PASSWORD']))
            return "Error: Password must contain only letters and numbers.";

        /*
        **
        **   End: Validation
        **
        */

        //Fill auto-generated values.

		$values['USE_RANK'] = 1;
    	$values['USE_SCORE'] = $values['USE_KILLS'] = $values['USE_DEATHS'] = $values['USE_ASSISTS'] = $values['USE_ACCURACY'] = $values['USE_ADMIN'] = 0;
    	
    	$user_exists = $model->user_exists($values['USE_LOGIN'],$values['USE_EMAIL']);

    	if(!$user_exists)//If user doesn't exist we register the new user.
    	{
    		$registration = $model->register_user($values);
    		echo $registration;
    	}else{
    		echo "User Already Exists";
    	}

    	
    }
}

?>
