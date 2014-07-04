<?php include('header.php'); ?>
	
    <div id="content">
        
        <form action='<?php echo "Auth/signup";?>' method='post'>
        	<?php echo $signup_login;?><input type='text' name='USE_LOGIN'><br/><br/>
        	<?php echo $signup_password;?><input type='text' name='USE_PASSWORD'><br/><br/>
        	<?php echo $signup_password_confirm;?><input type='text'><br/><br/>
        	<?php echo $signup_name;?><input type='text' name='USE_NAME'><br/><br/>
        	<?php echo $signup_nickname;?><input type='text' name='USE_NICKNAME'><br/><br/>
        	<?php echo $signup_lastname;?><input type='text' name='USE_LASTNAME'><br/><br/>
        	<?php echo $signup_email;?><input type='text' name='USE_EMAIL'><br/><br/>
        	<?php echo $signup_sex;?><input type='text' name='USE_SEX'><br/><br/>
        	<?php echo $signup_picture;?><input type='text' name='USE_PICTURE'><br/><br/>
        	<?php echo $signup_birthdate;?><input type='text' name='USE_BIRTHDATE'><br/><br/>
        	<?php echo $signup_city;?><input type='text' name='USE_CITY'><br/><br/>
        	<?php echo $signup_state;?><input type='text' name='USE_STATE'><br/><br/>
        	<?php echo $signup_country;?><input type='text' name='USE_COUNTRY'><br/><br/>
        	<input type='submit' value='<?php echo $signup_button;?>'>
        </form>
        <hr>
        <form action='<?php echo "Auth/recover_password";?>' method='post'>
            <?php echo $signup_email;?><input type='text' name='USE_EMAIL'><br/><br/>
            <?php echo $signup_birthdate;?><input type='text' name='USE_BIRTHDATE'><br/><br/>
            <input type='submit' value='<?php echo $recover_pw_button;?>'>
        </form>
        
    </div>

<?php include('footer.php'); ?>