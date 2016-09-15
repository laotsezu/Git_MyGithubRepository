<?php 
	abstract class BaseStatistic extends iBNC{
		var $types;
		var $descriptors;

		static $TAG = "ModelStatisticGoods: ";
		function __construct(){
			$this->setTypes();
			$this->setDescriptors();
		}
		abstract function setDescriptors();
		abstract function setTypes();
		abstract function getTopicName();
		abstract function getEnglishName();
		/*
			$where = array(	
				column
				, condition
				,operator
			);
		 */
		function get($table,$column="*",$where=null,$groupBy=null,$limit=null,$orderBy=null){
			$model = $this->getModel($table);
			if($where){
				$model->where($where[0],$where[1],$where[2]);
			}
			if($groupBy){
				$model->groupBy($groupBy);
			}
			
			if($orderBy){
				$model->orderBy($orderBy,"DESC");
			}

			$from_to = $limit ? array(0,$limit) : null;

			$result = $model->get(null,$from_to,$column);

			return $result;
		}
		/*
			//Pattern :
				$table_array = array(
					array(
					//this is first table 
						"tablename" => ""
					)
					,
					array(
						"tablename" => ""
						,"jointype" => ""
						,"on" => ""
					)
				);
		 */
		function getWithJoin($table_array,$column="*",$where=null,$groupBy=null,$limit=null,$orderBy=null){
			$model = $this->getModel($table_array[0]["tablename"]);

			for($i = 1;$i < count($table_array); $i++){
				$table = $table_array[$i];
				$model->join($table["tablename"]." ".$table["tablename"],$table["on"],$table["jointype"] ? $table["jointype"] : "LEFT");
			}
			
			if($where){
				$model->where($where[0],$where[1],$where[2]);
			}

			if($groupBy){
				$model->groupBy($groupBy);
			}

			if($orderBy){
				$model->orderBy($orderBy,"DESC");
			}

			$from_to = $limit ? array(0,$limit) : null;

			$result = $model->get(null,$from_to,$column);

			return $result;
		}
		function getOne($table,$column="*",$where=null){
			$model = $this->getModel($table);
			if($where){
				$model->where($where[0],$where[1],$where[2]);
			}
			$result = $model->getOne(null,$column);

			return $result;
		}
		function handleResponse($result){
			if($result){
				$response["status"] = true;
				$response["count"] = count($result);
				$response["items"] = $result;
			}
			else{
				$response["status"] = false;
			}

			return $response;
		}
		function getType($position){
			if($position < count($this->types)){
				return $this->types[$position];
			}
			else {
				$response["status"] = false;
				$response["message"] = self::$TAG."position >= count(types)";
				die(json_encode($response));		
			}
		}
		function getDescriptor($position){
			if($position < count($this->descriptors)){
				return $this->descriptors[$position];
			}
			else {
				$response["status"] = false;
				$response["message"] = self::$TAG."position >= count(descriptors)";
				die(json_encode($response));		
			}
		}
		function getTypes(){
			return $this->types;
		}
		function getDescriptors(){
			return $this->descriptors;
		}
		
		function getStatisticResult($position,$from,$to){
			$this->getType($position);
			$method = "showType" . $position;
			return $this->$method($from,$to);
		}
	}
	
?>