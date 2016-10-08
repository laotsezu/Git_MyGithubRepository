<?php 
use Symfony\Component\DomCrawler\Crawler;
require_once DIR_CLASS.'vendor/autoload.php';

	class MyDOMDocument{
		var $doc;
		var $xpath;
		function init($url,$removeNodes=null){
			$html = $this->getPageContent($url);
			$this->doc = new DOMDocument();
			$this->doc->preserveWhiteSpace = false;  
			@$this->doc->loadHTML('<?xml encoding="UTF-8">' .$html); 

			if(isset($removeNodes)){
				foreach ($removeNodes as $key => $value) {  
					if( substr($value,0,1) == '#'){
						$ele = $this->doc->getElementById(str_replace('#', '', $value)); 
					}
					else {
						$ele = $this->doc->getFirstElementByClassName(str_replace('.', '', $value))->item(0); 
					} 
						
					$ele->parentNode->removeChild($ele); 
				}
				$this->doc->saveHTML();
			} 

			$this->xpath = new DomXPath($this->doc);
			
		}

		 
		function getFirstElementByClassName($className) {
    		return $this->xpath->query("//*[@class='$className']")->item(0);
    	}
    	function getElementById($id){
    		return $this->xpath->query("//*[@id='$id']")->item(0);
    	}
    	function getElementsByClassName($className){
    		return $this->xpath->query("//*[@class='$className']");
    	} 

		function getPageContent($url)
		{  
			$client = new GuzzleHttp\Client(); 
			$res = $client->get($url);
			if($res->getStatusCode() === 200){
				return $res->getBody()->getContents();
			}
			else{
				return null;
			}
		}
	}


?>