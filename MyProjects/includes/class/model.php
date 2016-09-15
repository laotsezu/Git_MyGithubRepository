<?php
/**
 * @Project ID BNC
 * @File /includes/class/model.php
 * @Author Quang Chau Tran (quangchauvn@gmail.com)
 * @Createdate 09/03/2014, 10:54 AM
 */
if(!defined('BNC_CODE')) {
    exit('Access Denied');
}
class Model{	
	private $name,$db; 
	public function __construct($db,$name=null){ 			
		if($name==null)			
			$this->name = get_class($this);		
		else			
			$this->name = $name;
		$this->db = $db;
	}
	/*
	 *@data  array
	 */		
	public function insert($data){ 	  
		return $this->db->insert($this->name,$data); 
	}		
	/*
	 *@data  array 
	 */
	public function update($data){  
		return $this->db->update($this->name,$data);
	}		
	/* 
	 */
	public function delete(){ 	 
		return $this->db->delete($this->name);
	}
     public function rawQuery ($query, $bindParams = null, $sanitize = true){  
          return $this->db->rawQuery ($query, $bindParams, $sanitize);
     } 	
	/*
	 *@columns array(string) so truong can lay
	 *@numRows int (0,limit), array (v0,v1)
	 */
	public function get($as = null,$numRows = null,$columns='*'){		
		if($as == null)
			return $this->db->get($this->name, $numRows, $columns);
		else
			return $this->db->get($this->name." ".$as, $numRows, $columns);
	}		
 	/**
     * A convenient SELECT * function to get one record.
     *
     * @param string  $tableName The name of the database table to work with.
     *
     * @return array Contains the returned rows from the select query.
     */
     public function getOne($as=null,$columns = '*'){
          if($as == null)
               return $this->db->getOne($this->name,$columns);
          else
               return $this->db->getOne($this->name." ".$as, $columns);
     }
      /**
     * This methods returns the ID of the last inserted item
     * author: Huong Nguyen Ba (nguyenbahuong156@gmail.com)
     * create time 19/08/2014 01:44 AM
     * @return integer The last inserted item ID.
     */
     public function getLastId()
     {
             return $this->db->getInsertId();
     }
     /**
     * This method allows you to specify multiple (method chaining optional) AND WHERE statements for SQL queries.
     *
     * @uses $MySqliDb->where('id', 7)->where('title', 'MyTitle');
     *
     * @param string $whereProp  The name of the database field.
     * @param mixed  $whereValue The value of the database field.
     *
     * @return MysqliDb
     */
 	public function where($whereProp, $whereValue = null, $operator = null){
 		$this->db->where($whereProp, $whereValue, $operator);
          return $this;
 	}
 	/**
     * This method allows you to specify multiple (method chaining optional) OR WHERE statements for SQL queries.
     *
     * @uses $MySqliDb->orWhere('id', 7)->orWhere('title', 'MyTitle');
     *
     * @param string $whereProp  The name of the database field.
     * @param mixed  $whereValue The value of the database field.
     *
     * @return MysqliDb
     */
 	public function orWhere($whereProp, $whereValue = null, $operator = null){
 		$this->db->orWhere($whereProp, $whereValue, $operator);
          return $this;
 	}
 	 /**
     * This method allows you to specify multiple (method chaining optional) ORDER BY statements for SQL queries.
     *
     * @uses $MySqliDb->orderBy('id', 'desc')->orderBy('name', 'desc');
     *
     * @param string $orderByField The name of the database field.
     * @param string $orderByDirection Order direction.
     *
     * @return MysqliDb
     */
 	public function orderBy($orderByField, $orderbyDirection = "DESC"){
 		$this->db->orderBy($orderByField,$orderbyDirection);
          return $this;
 	}
 	 /**
     * This method allows you to specify multiple (method chaining optional) GROUP BY statements for SQL queries.
     *
     * @uses $MySqliDb->groupBy('name');
     *
     * @param string $groupByField The name of the database field.
     *
     * @return MysqliDb
     */
     public function startTransaction(){
          $this->db->startTransaction();
     }
     public function commit(){
          $this->db->commit();
     }
     public function rollback(){
          $this->db->rollback();
     }
 	public function groupBy($groupByField){
 		$this->db->groupBy($groupByField);
          return $this;
 	}
 	/**
     * This method allows you to concatenate joins for the final SQL statement.
     *
     * @uses $MySqliDb->join('table1', 'field1 <> field2', 'LEFT')
     *
     * @param string $joinTable The name of the table.
     * @param string $joinCondition the condition.
     * @param string $joinType 'LEFT', 'INNER' etc.
     *
     * @return MysqliDb
     */
 	public function join($as, $joinCondition, $joinType = ''){
          $this->db->join($as, $joinCondition, $joinType);
          return $this;
     }
 	
     public function getLastError(){
          return  $this->db->getLastError(); 
     } 
     /**
     * A convenient num_rows
     *
     * @param string  $tableName The name of the database table to work with. 
     *
     * @return int number rows from the select query.
     */
     public function num_rows(){
          return  $this->db->num_rows($this->name);
     }
     /**
     * Copy table for multilang  
     * Copy table lang_tablenam from vi_tablename and ADD id_lang (id of item from vi_tablenam)
     */
     private function CopyTableLang(){
          //return;
          if( $this->db->table_exist($this->name) )  
          {
               return;
          }
          else
          {
               //chua co bang, tien hanh nhan ban
               $langkey = substr($this->name,0,3);
               $tabledefault = str_replace($langkey, 'vi_', $this->name);  
               if( $this->db->table_exist($tabledefault) ) {
                    $this->db->rawQuery("CREATE TABLE `$this->name` LIKE `$tabledefault`");
                    //$this->db->rawQuery("ALTER TABLE `$this->name` ADD id_lang int(11)");
                    //$this->db->rawQuery("ALTER TABLE `$this->name` ADD INDEX (`id_lang`)");
               } 
          }

     }
}
?>
