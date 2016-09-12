<?php

require '../include/vendor/autoload.php';
require_once '../include/DbOperation.php';

//Creating a slim instance
$app = new \Slim\Slim();

/* *
 * URL: http://localhost/App/v1/createuser
 * Parameters: apikey, login, password, admin, active, name, surname, cpf, email, phone, institution, address
 * Authorization: Put API Key in Request Header
 * Method: POST
 * */
$app->post('/createuser', 'authenticateUser', function () use ($app) {
    verifyRequiredParams(array('login', 'password', 'admin', 'active', 'name', 'surname', 'cpf', 'email', 'phone', 'institution', 'address'));
    $response = array();
    $login = $app->request->post('login');
    $password = $app->request->post('password');
	$admin = $app->request->post('admin');
    $active = $app->request->post('active');
	$name = $app->request->post('name');
    $surname = $app->request->post('surname');
	$cpf = $app->request->post('cpf');
    $email = $app->request->post('email');
	$phone = $app->request->post('phoe');
    $institution = $app->request->post('institution');
	$address = $app->request->post('address');
	
    $db = new DbOperation();
    $res = $db->createUser($login, $password, $admin, $active, $name, $surname, $cpf, $email, $phone, $institution, $address);
    if ($res == 0) {
        $response["error"] = false;
        $response["message"] = "User successfully registered";
        echoResponse(201, $response);
    } else if ($res == 1) {
        $response["error"] = true;
        $response["message"] = "Oops! An error occurred while registereing";
        echoResponse(200, $response);
    } else if ($res == 2) {
        $response["error"] = true;
        $response["message"] = "Sorry, this user already existed";
        echoResponse(200, $response);
    }
});

/* *
 * URL: http://localhost/App/v1/createstation
 * Parameters: name, token
 * Method: POST
 * */
$app->post('/createstation', function () use ($app) {
    verifyRequiredParams(array('name', 'apiKey'));
    $response = array();
    $name = $app->request->post('name');
    $token = $app->request->post('apiKey');
	
    $db = new DbOperation();
    $res = $db->createStation($name, $token);
    if ($res == 0) {
		$station = $db->getStation($name);
        $response["error"] = false;
		$response["id"] = $station["id"];
        echoResponse(201, $response);
    } else if ($res == 1) {
        $response["error"] = true;
        $response["message"] = "Oops! An error occurred while registereing";
        echoResponse(200, $response);
    } else if ($res == 2) {
        $response["error"] = true;
        $response["message"] = "Sorry, this station already existed";
        echoResponse(200, $response);
    }
});

/* *
 * URL: http://localhost/App/v1/createsensor
 * Parameters: sensorname, magnitude, unit, station_id
 * Method: POST
 * */
$app->post('/createsensor', function () use ($app) {
    verifyRequiredParams(array('sensorname', 'magnitude', 'unit', 'station_id'));
	$sensorname = $app->request->post('sensorname');
    $magnitude = $app->request->post('magnitude');
	$unit = $app->request->post('unit');
	$station_id = $app->request->post('station_id');
	
    $db = new DbOperation();
    $response = array();
    if ($db->createSensor($sensorname, $magnitude, $unit, $station_id)) {
        $sensor = $db->getSensor($sensorname);
        $response['error'] = false;
        $response['id'] = $sensor['id'];
        $response['sensorname'] = $sensor['name'];
        $response['magnitude'] = $student['magnitude'];
		$response['unit'] = $student['unit'];
		$response['station_id'] = $sensor['station_id'];
    } else {
        $response['error'] = true;
        $response['message'] = "Invalid station name";
    }
    echoResponse(200, $response);
});

/* *
 * URL: http://localhost/App/v1/submitreading
 * Parameters: data
 * Authorization: Put API Key in Request Header
 * Method: POST
 * */
$app->post('/submitreading', 'authenticateStation', function () use ($app) {
    verifyRequiredParams(array('data'));
    $response = array();
    $data = $app->request()->post('data');
	
	$readings = json_decode(utf8_encode($data), true);
	if($readings){
		$db = new DbOperation();
		$result = $db->insertReading($readings);
		$response = array();
		if($result){
			$response['error'] = false;
			$response['message'] = "Reading submitted successfully";
		}else{
			$response['error'] = true;
			$response['message'] = "Could not submit reading";
		}
		echoResponse(200,$response);
	}
	else{
        $response['error'] = true;
        $response['message'] = "Data is not valid JSON";
		echoResponse(200,$response);
    }
	
	
});


/* *
 * URL: http://localhost/App/v1/readers/<sensor_id>
 * Parameters: none
 * Authorization: Put API Key in Request Header
 * Method: GET
 * */
$app->get('/readers/:id', 'authenticateUser', function($sensor_id) use ($app){
    $db = new DbOperation();
    $result = $db->getReaders($sensor_id);
    $response = array();
    $response['error'] = false;
    $response['assignments'] = array();
    while($row = $result->fetch_assoc()){
        $temp = array();
        $temp['id']=$row['id'];
        $temp['name'] = $row['name'];
        $temp['details'] = $row['details'];
        $temp['completed'] = $row['completed'];
        $temp['faculty']= $db->getFacultyName($row['faculties_id']);
        array_push($response['assignments'],$temp);
    }
    echoResponse(200,$response);
});


/* *
 * URL: http://localhost/App/v1/submitreading/<reader_id>
 * Parameters: data
 * Authorization: Put API Key in Request Header
 * Method: PUT
 * */

$app->put('/submitreading/:id', 'authenticateUser', function($reader_id) use ($app){
	verifyRequiredParams(array('data'));
    $db = new DbOperation();
    $result = $db->updateReading($reader_id, $data);
    $response = array();
    if($result){
        $response['error'] = false;
        $response['message'] = "Assignment submitted successfully";
    }else{
        $response['error'] = true;
        $response['message'] = "Could not submit assignment";
    }
    echoResponse(200,$response);
});


function echoResponse($status_code, $response)
{
    $app = \Slim\Slim::getInstance();
    $app->status($status_code);
    $app->contentType('application/json');
    echo json_encode($response);
}


function verifyRequiredParams($required_fields)
{
    $error = false;
    $error_fields = "";
    $request_params = $_REQUEST;

    if ($_SERVER['REQUEST_METHOD'] == 'PUT') {
        $app = \Slim\Slim::getInstance();
        parse_str($app->request()->getBody(), $request_params);
    }

    foreach ($required_fields as $field) {
        if (!isset($request_params[$field]) || strlen(trim($request_params[$field])) <= 0) {
            $error = true;
            $error_fields .= $field . ', ';
        }
    }

    if ($error) {
        $response = array();
        $app = \Slim\Slim::getInstance();
        $response["error"] = true;
        $response["message"] = 'Required field(s) ' . substr($error_fields, 0, -2) . ' is missing or empty';
        echoResponse(400, $response);
        $app->stop();
    }
}

function authenticateUser(\Slim\Route $route)
{
    $headers = apache_request_headers();
    $response = array();
    $app = \Slim\Slim::getInstance();

    if (isset($headers['Authorization'])) {
        $db = new DbOperation();
        $apiKey = $headers['Authorization'];
        if ((strlen($apiKey) !== 20) || !$db->isValidUser($apiKey)) {
            $response["error"] = true;
            $response["message"] = "Access Denied. Invalid Api key";
            echoResponse(401, $response);
            $app->stop();
        }
    } else {
        $response["error"] = true;
        $response["message"] = "Api key is misssing";
        echoResponse(400, $response);
        $app->stop();
    }
}

function authenticateStation(\Slim\Route $route)
{
    $headers = apache_request_headers();
    $response = array();
    $app = \Slim\Slim::getInstance();

    if (isset($headers['Authorization'])) {
        $db = new DbOperation();
        $apiKey = $headers['Authorization'];
        if ((strlen($apiKey) !== 20) || !$db->isValidStation($apiKey)) {
            $response["error"] = true;
            $response["message"] = "Access Denied. Invalid Api key";
            echoResponse(401, $response);
            $app->stop();
        }
    } else {
        $response["error"] = true;
        $response["message"] = "Api key is misssing";
        echoResponse(400, $response);
        $app->stop();
    }
}

$app->run();