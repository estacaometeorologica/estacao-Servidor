<?php

class DbOperation
{
    private $con;

    function __construct()
    {
        require_once dirname(__FILE__) . '/DbConnect.php';
        $db = new DbConnect();
        $this->con = $db->connect();
    }

    //Method to register a new user
    public function createUser($login, $pass, $admin, $active, $name, $surname, $cpf, $email, $phone, $institution, $address){
        if (!$this->isStudentExists($username)) {
            $password = md5($pass);
            $apikey = $this->generateApiKey();
            $stmt = $this->con->prepare("INSERT INTO usuario(apiKey, login, senha, nome, sobrenome, cpf, email, telefone, instituicao, endereco, admin, ativo) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            $stmt->bind_param("ssssssssssii", $apiKey, $login, $password, $name, $surname, $cpf, $email, $phone, $institution, $address, $admin, $active);
            $result = $stmt->execute();
            $stmt->close();
            if ($result) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 2;
        }
    }

    //Method to let a student log in
    public function userLogin($login,$pass){
        $password = md5($pass);
        $stmt = $this->con->prepare("SELECT * FROM usuario WHERE login=? and password=?");
        $stmt->bind_param("ss",$login,$password);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        return $num_rows>0;
    }

    //Method to update user status
    public function updateUserStatus($id, $active){
        $stmt = $this->con->prepare("UPDATE usuario SET active = ? WHERE id=?");
        $stmt->bind_param("ii",$active,$id);
        $result = $stmt->execute();
        $stmt->close();
        if($result){
            return true;
        }
        return false;
    }
	
	//Method to insert readings
    public function insertReading($readings){
		foreach($readings as $reading){
			$reader_id = $reading["reader_id"];
			$stmt = $this->con->prepare("SELECT idLeitor FROM leitor WHERE idLeitor=?");
			$stmt->bind_param("i",$reader_id);
			$stmt->execute();
			$stmt->store_result();
			$num_rows = $stmt->num_rows;
			$stmt->close();
			if ($num_rows === 0) return false;
			$value = $reading["value"];
			$stmt = $this->con->prepare("INSERT INTO leitura (valor,data,idLeitor) VALUES (?,now(),?)");
			$stmt->bind_param("si",$value,$reader_id);
			$stmt->execute();
			$stmt->close();
		}
		return true;
    }
	
    //Method to get all the sensors of a particular station
    public function getSensors($station_id){
        $stmt = $this->con->prepare("SELECT * FROM sensor WHERE idEstacao=?");
        $stmt->bind_param("i",$station_id);
        $stmt->execute();
        $sensors = $stmt->get_result();
        $stmt->close();
        return $sensors;
    }

    //Method to get sensor details
    public function getSensor($sensor_id){
        $stmt = $this->con->prepare("SELECT * FROM sensor WHERE idSensor=?");
        $stmt->bind_param("s",$sensor_id);
        $stmt->execute();
        $sensor = $stmt->get_result()->fetch_assoc();
        $stmt->close();
        return $sensor;
    }

    //Method to fetch all stations from database
    public function getAllStations(){
        $stmt = $this->con->prepare("SELECT * FROM estacao");
        $stmt->execute();
        $stations = $stmt->get_result();
        $stmt->close();
        return $stations;
    }

    //Method to check the sensor exists or not
    private function isSensorExists($sensor_id) {
        $stmt = $this->con->prepare("SELECT idSensor from sensors WHERE idSensor=?");
        $stmt->bind_param("s", $sensor_id);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        return $num_rows > 0;
    }
	
	//Method to check the station exists or not
    private function isStationExists($station_id) {
        $stmt = $this->con->prepare("SELECT idEstacao from estacao WHERE idEstacao=?");
        $stmt->bind_param("s", $station_id);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        return $num_rows > 0;
    }

    //Checking the user is valid or not by api key
    public function isValidUser($apiKey) {
        $stmt = $this->con->prepare("SELECT idUsuario from usuario WHERE apiKey=?");
        $stmt->bind_param("s", $apiKey);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        return $num_rows > 0;
    }
	
	//Checking the station is valid or not by api key
    public function isValidStation($apiKey) {
        $stmt = $this->con->prepare("SELECT idEstacao from estacao WHERE apiKey=?");
        $stmt->bind_param("s", $apiKey);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        return $num_rows > 0;
    }

    //Method to generate a unique api key every time
    private function generateApiKey(){
        return md5(uniqid(rand(), true));
    }
}