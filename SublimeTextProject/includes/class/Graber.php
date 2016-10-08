<?php 
	class Graber{
		var $url;
		var $base_url;
		var $methodsOf;
		var $inputOf;
		function init($url,$base_url,$methodsOf,$inputOf,$removeNodes=null){
			$this->url = $url;
			$this->base_url = $base_url;
			$this->methodsOf = $methodsOf;
			$this->inputOf = $inputOf;
			$this->removeNodes = $removeNodes;
		}
		function grabListArticle(){
			$array_link = $this->grabListLinkDetail();
			$result  = array();

			for($i = 0; $i < count($array_link) ; $i++){
				$result[$i] = $this->grabDetailArticle($array_link[$i]);
			}

			return $result;
		}
		function grabDetailArticle($detail_url){

			$mDomDocument = new MyDOMDocument();
			$mDomDocument->init($detail_url,$this->removeNodes);

			$result = array();

			$index = array("brief","title","image","content");
			for($i = 0 ; $i < count($index) ; $i++){
				$method = $this->methodsOf[$index[$i]];
				$input = $this->inputOf[$index[$i]];
				$output = $mDomDocument->$method($input);

				$result[$index[$i]] = $output;
			}

			$output = array();

			$output["title"] = $this->buildOutputFromDOMNode($result["title"]);
			$output["brief"] = strip_tags($this->buildOutputFromDOMNode($result["brief"]));
			$output["image"] = $result["image"]->getAttribute("src");
			$output["content"] = $this->buildOutputFromDOMNode($result["content"]);
			$output["link"] = $detail_url;

			return $output;
		}
		function grabListLinkDetail(){
			$result = array();
			$mDomDocument = new MyDOMDocument($this->url);
			$mDomDocument->init($this->url);

			$method = $this->methodsOf["list_article"];
			$input = $this->inputOf["list_article"];

			$list = $mDomDocument->$method($input);

			$result = array();

			for($i = 0; $i < $list->length;$i++){
				$entry = $list->item($i);
				$result[$i] = $entry->getAttribute("href");
				if( substr($result[$i],0,4) != 'http' ){
					$result[$i] = $this->base_url . $result[$i];
				}
			}

			return $result;
		}
	    function buildOutputFromDOMNode(DOMNode $element){
	    	$innerHTML = ""; 
		    $children  = $element->childNodes;

		    foreach ($children as $child) { 
		        $innerHTML .= $element->ownerDocument->saveHTML($child);
		    }

		    return $innerHTML; 
	    }
	}
?>