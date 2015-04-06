<?php
require_once('model.php');
require_once('Database.php');
	
if(isset($_GET['lastChecked']) && !empty($_GET['lastChecked'])) {
	$lastChecked = $_GET['lastChecked'];
} else {
	$lastChecked = false;
}

$json_array = ShoppingListModel::get_all_items($lastChecked);
echo json_encode($json_array,JSON_NUMERIC_CHECK);
?>