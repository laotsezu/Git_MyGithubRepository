<?php
/**
 * @Project ID BNC
 * @File /includes/class/tempalte.php
 * @Author Quang Chau Tran (quangchauvn@gmail.com)
 * @Createdate 09/03/2014, 10:51 AM
 */
if(!defined('BNC_CODE')) {
    exit('Access Denied');
}
class Template {
	private $dir_theme; 
	private $dir_file; 
	private $g; 

	public function __construct(){ 
		global $_B;		
		$this->dir_theme = DIR_THEME.$_B['theme'].'/';
		$this->dir_file = DIR_TMP.'tpl/';
	}
	public function load($name){ 
		global $_B;	 
		$them = $_B['theme'].'/'.$name;
		
		$objfile = $this->dir_file.$_B['theme'].'_'.$name.'.php';
	 	
	 	if(!file_exists($objfile) || 1 ) {  
			$this->parse_template($name);
		} 	
		return $objfile;	
	}
	private function parse_template($tpl) { 
		global $_B;
		$tplfile = $this->dir_theme.$tpl.'.htm';
		$objfile = $this->dir_file.$_B['theme'].'_'.$tpl.'.php';
	 
		$template = $this->sreadfile($tplfile);
		if(empty($template)) {
			exit("Template file : $tplfile Not found or have no access!");
		}
	 
		 
	 	$template = preg_replace("/\<\!\-\-\{temp\s+([a-z0-9_\/]+)\}\-\-\>/ie", "\$this->readtemplate('\\1')", $template); 
	 	$template = preg_replace("/\<\!\-\-\{looptemp\s+([a-z0-9_\/]+)\}\-\-\>/ie", "\$this->looptemp('\\1')", $template);
	 	$template = preg_replace("/\{lang\s+(\w+?)\s*\}/i", "<? echo lang('\\1');?>", $template);
	 	$template = preg_replace("/\{show_ss\s+(\w+?)\s*\}/i", "<? echo show_ss('\\1');?>", $template);
	 	$template = preg_replace("/\{lang\s+(\w+?)\s+([a-z0-9_,]+)\s*\}/i", "<? echo lang('\\1','\\2');?>", $template);
	 	$template = preg_replace("/\{lang\s+(\w+?)\s+\{\\\$([a-z0-9_,\[\]\']+)\}\s*\}/i", "<? echo lang('\\1',@qc@\\2);?>", $template);
	 	$template = preg_replace("/\{lang\s+(\w+?)\s+(\w+)\{\\\$([a-z0-9_,\[\]\']+)\}\s*\}/i", "<? echo lang('\\1','\\2'.@qc@\\3);?>", $template);
	 	$template = preg_replace("/\{lang\s+(\w+)\{\\\$([a-z0-9_,\[\]\']+)\}\s*\}/i", "<? echo lang('\\1'.@qc@\\2);?>", $template);
		
		$template = preg_replace("/\<\!\-\-\{eval\s+(.+?)\s*\}\-\-\>/ies", "\$this->evaltags('\\1')", $template);
		$var_regexp = "((\\\$[a-zA-Z_\x7f-\xff][a-zA-Z0-9_\x7f-\xff]*)(\[[a-zA-Z0-9_\-\.\"\'\[\]\$\x7f-\xff]+\])*)";
		$template = preg_replace("/\<\!\-\-\{(.+?)\}\-\-\>/s", "{\\1}", $template);
		$template = preg_replace("/([\n\r]+)\t+/s", "\\1", $template);
		$template = preg_replace("/(\\\$[a-zA-Z0-9_\[\]\'\"\$\x7f-\xff]+)\.([a-zA-Z_\x7f-\xff][a-zA-Z0-9_\x7f-\xff]*)/s", "\\1['\\2']", $template);
		$template = preg_replace("/\{(\\\$[a-zA-Z0-9_\[\]\'\"\$\.\x7f-\xff]+)\}/s", "<?=\\1?>", $template);
		$template = preg_replace("/$var_regexp/es", "\$this->addquote('<?=\\1?>')", $template);
		$template = preg_replace("/\<\?\=\<\?\=$var_regexp\?\>\?\>/es", "\$this->addquote('<?=\\1?>')", $template);
		 
		$template = preg_replace("/\{elseif\s+(.+?)\}/ies", "\$this->stripvtags('<?php } elseif(\\1) { ?>','')", $template);
		$template = preg_replace("/\{else\}/is", "<?php } else { ?>", $template);
		 
		for($i = 0; $i < 10; $i++) {
			$template = preg_replace("/\{loop\s+(\S+)\s+(\S+)\}(.+?)\{\/loop\}/ies", "\$this->stripvtags('<?php if(is_array(\\1)) { foreach(\\1 as \\2) { ?>','\\3<?php } } ?>')", $template);
			$template = preg_replace("/\{loop\s+(\S+)\s+(\S+)\s+(\S+)\}(.+?)\{\/loop\}/ies", "\$this->stripvtags('<?php if(is_array(\\1)) { foreach(\\1 as \\2 => \\3) { ?>','\\4<?php } } ?>')", $template);
			$template = preg_replace("/\{if\s+(.+?)\}(.+?)\{\/if\}/ies", "\$this->stripvtags('<?php if(\\1) { ?>','\\2<?php } ?>')", $template);
		}
		 
		$template = preg_replace("/\{([a-zA-Z_\x7f-\xff][a-zA-Z0-9_\x7f-\xff]*)\}/s", "<?=\\1?>", $template);
		if(!empty($this->g['block_search'])) {
			$template = str_replace($this->g['block_search'], $this->g['block_replace'], $template);
		}
		$template = preg_replace("/ \?\>[\n\r]*\<\? /s", " ", $template);
		$template = str_replace('@qc@', '$', $template);
		$template = str_replace('body_content_module', '<?php include_once $content_module; ?>', $template);
		$template = str_replace('body_header', '<?php include_once $_B[\'temp\']->load(\'header\');  ?>', $template);
		$template = str_replace('body_footer', '<?php include_once $_B[\'temp\']->load(\'footer\');  ?>', $template);
		$template = str_replace('body_menu_left', '<?php include_once $_B[\'temp\']->load(\'menu_left\');  ?>', $template);
	 	
	 // 	$template = preg_replace( '/<!--(.|\s)*?-->/' , '' , $template);
		// $template = preg_replace("/[^\"|'|http:\\/\\/|https:\\/\\/]\\/\\/(.*)/i", '', $template);
		// $search   = array("/\>[^\S ]+/s", '/[^\S ]+\</s', '/(\s)+/s');
		// $replace  = array('>', '<', '\\1');
		// $template = preg_replace($search, $replace, $template);
		// $template = str_replace(array("\n","\r","\t"),'',$template);
		// $template = preg_replace(array('/<!--(.*)-->/Uis',"/[[:blank:]]+/"),array('',' '),str_replace(array("\n","\r","\t"),'',$template));
		
	 
		$template ="<?php 
/**
 * @Project BNC v2 -> Adminuser
 * @File $objfile 
 * @Author Quang Chau Tran (quangchauvn@gmail.com) 
 */
if(!defined('BNC_CODE')) {
    exit('Access Denied');
}
?>$template";
		
		 
		if(!$this->swritefile($objfile, $template)) {
			exit("File: $objfile can not be write!");
		}
	}
	private function sreadfile($filename) {
		$content = '';
		if(function_exists('file_get_contents')) {
			@$content = file_get_contents($filename);
		} else {
			if(@$fp = fopen($filename, 'r')) {
				@$content = fread($fp, filesize($filename));
				@fclose($fp);
			}
		}
		return $content;
	}
	private function readtemplate($name) {
		global $_QC;  
		$tplfile = $this->dir_theme.$name.'.htm';
		$content = $this->sreadfile($tplfile);
		return $content;
	}
	private function swritefile($filename, $writetext, $openmod='w') {
		if(@$fp = fopen($filename, $openmod)) {
			flock($fp, 2);
			fwrite($fp, $writetext);
			fclose($fp);
			chmod($filename, 0664);
			return true;
		} else {
			return false;
		}
	} 
	private function addquote($var) {
		return str_replace("\\\"", "\"", preg_replace("/\[([a-zA-Z0-9_\-\.\x7f-\xff]+)\]/s", "['\\1']", $var));
	}
	private function stripvtags($expr, $statement='') {
		$expr = str_replace("\\\"", "\"", preg_replace("/\<\?\=(\\\$.+?)\?\>/s", "\\1", $expr));
		$statement = str_replace("\\\"", "\"", $statement);
		return $expr.$statement;
	}
	private function looptemp($parameter) 
	{ 
		$this->g['i']++;
		$search = "<!--TEMPWEB_TAG_{$this->g['i']}-->";
		$this->g['block_search'][$this->g['i']] = $search;
		$this->g['block_replace'][$this->g['i']] = "<? include \$temp->load($parameter) ?>";
		return $search;
	}
	private function evaltags($php) { 

		@$this->g['i']++;
		$search = "<!--EVAL_TAG_{$this->g['i']}-->";
		$this->g['block_search'][$this->g['i']] = $search;
		$this->g['block_replace'][$this->g['i']] = "<?php ".$this->stripvtags($php)." ?>";
		
		return $search;
	}
}