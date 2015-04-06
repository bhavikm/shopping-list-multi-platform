<?php
class Database {
	private static $dsn = 'mysql:host=localhost;dbname=bhavikmo_fit4009';
	private static $username = 'bhavikmo_fit4009';
	private static $password = 'a56193swt94201603';
	private static $db;
	
	private function __construct() {}
	
	public static function getDB () {
		if (!isset(self::$db)) {
			try {
				self::$db = new PDO (self::$dsn,
									 self::$username,
									 self::$password);
			} catch (PDOException $e) {
				$error_message = $e->getMessage();
				echo $error_message;
				exit();
			}
		}
		return self::$db;
	}
}
?>