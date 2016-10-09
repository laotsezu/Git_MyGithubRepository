<?php
/**
 * @Project BNC v2 -> Frontend
 * @File /includes/class/cache.php 
 * @Author Quang Chau Tran (quangchauvn@gmail.com)
 * @Createdate 10/24/5014, 01:18 AM
 */
if(!defined('BNC_CODE')) {
    exit('Access Denied');
}
class CacheBNC{
	/*
	 * config memcache
	 */
	private $memcache_port = 11211;
	private $memcache_server = array('localhost');
	/*
	 * config redis
	 */
	private $redis_port = 6379;
	private $redis_server = array('127.0.0.1');
	/*
	 * other
	 */
	private $drive;
	public $cache;
	public function __construct($drive="memcache"){ 
		$this->drive = $drive;	
		return $this->$drive();			
	}   
	/*
	 * drives memcache
	 */
	private function memcache(){
		$this->cache = new Memcache;  
		foreach ($this->memcache_server as $key => $value) {
			$this->cache->addServer($value,$this->memcache_port);
		}
		//return $this->cache; 
	}
	private function memcache_get($key,$compressed){
		return $this->cache->get($key,$compressed);
	}
	private function memcache_set($key,$value,$compressed,$expire){
		return $this->cache->set($key,$value,$compressed,$expire);
	}
	private function memcache_del($key){
		return $this->cache->delete($key);
	}
	/*
	 * drives redis
	 */
	private function redis(){
		$this->cache = new Redis();  
		foreach ($this->redis_server as $key => $value) {
			$this->cache->connect($value,$this->redis_port);
		}
		//return $this->cache; 
	}
	private function redis_get($key,$compressed){
		return $this->cache->get($key);
	}
	private function redis_set($key,$value,$compressed,$expire){
		return $this->cache->set($key,$value);
	}
	private function redis_del($key){
		return $this->cache->delete($key);
	}
	/*
	 * public functions
	 */
	public function get($key,$compressed = false){
		// return null;
		$function = $this->drive.'_get';
		return $this->$function($_SERVER['HTTP_HOST'].'_'.$key,$compressed); 
	}
	public function set($key,$value,$compressed = false,$expire = 0){
		$function = $this->drive.'_set';
		return $this->$function($_SERVER['HTTP_HOST'].'_'.$key,$value,$compressed,$expire);
	}
	public function del($key){
		$function = $this->drive.'_del';
		return $this->$function($_SERVER['HTTP_HOST'].'_'.$key);
	}
}

?>