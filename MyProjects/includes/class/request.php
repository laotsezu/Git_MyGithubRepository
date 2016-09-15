<?php
/**
 * @Project ID BNC
 * @File /includes/class/request.php
 * @Author Quang Chau Tran (quangchauvn@gmail.com)
 * @Createdate 09/03/2014, 10:53 AM
 */
if(!defined('BNC_CODE')) {
    exit('Access Denied');
}  

class Request
{

    const IS_HEADERS_SENT = 'Warning: Headers already sent';

    const INCORRECT_IP = 'Incorrect IP address specified';

    const SESSION_SAVE_PATH_NOT_SET = 'session.save_path directory not set! Please set your session.save_path in your system file';

    const INCORRECT_SESSION_ID = 'We\'re sorry, but you must have cookies enabled in your browser in order to use this part of the website. Please see your browser\'s documentation to learn how to enable cookies and return to the site once you have done so to continue';

    public $session_id;

    public $doc_root;

    public $site_url;

    public $base_siteurl;

    public $base_adminurl;

    public $server_name;

    public $server_protocol;

    public $server_port;

    public $my_current_domain;

    public $my_domains;

    public $headerstatus;

    public $session_save_path;

    public $cookie_path;

    public $cookie_domain;

    public $is_register_globals = false;

    public $is_magic_quotes_gpc = false;

    public $referer;

    public $referer_key;

    public $referer_host = '';

    public $referer_queries = false;

    public $request_uri;

    public $user_agent;

    public $search_engine = '';

    private $request_default_mode = "request";

    private $allow_request_mods = array( 
        'get', 'post', 'request'
    ); 

    private $secure = false;

    private $httponly = true;

    private $ip_addr;

    private $XSS_replaceString = '<x>';

    private $is_session_start = false;

    private $is_filter = false;

    private $str_referer_blocker = false;

    private $engine_allowed = array();

    private $disabletags = array( 
        "applet", "body", "basefont", "head", "html", "id", "meta", "xml", "blink", "link", "style", "script"/*, "iframe"*/, "frame", "frameset", "ilayer", "layer", "bgsound", "title", "base" 
    );

    /*  private $disabletags = array(
    "applet", "body", "basefont", "head", "html", "id", "meta", "xml", "blink", "link", "style", "script", "embed", "object", "iframe", "frame", "frameset", "ilayer", "layer", "bgsound", "title", "base" 
    );*/
    private $disabledattributes = array( 
        'action', 'background', 'codebase', 'dynsrc', 'lowsrc' 
    );

    private $disablecomannds = array( 
        "base64_decode", "cmd", "passthru", "eval", "exec", "system", "fopen", "fsockopen", "file", "file_get_contents", "readfile", "unlink" 
    );

    /**
     * Request::__construct()
     * 
     * @param mixed $config
     * @param mixed $ip
     * @return
     */
    
    public function __construct ()
    {
        
         
       $ip = $_SERVER['REMOTE_ADDR'];
        $this->ip_addr = ip2long( $ip );
        if ( $this->ip_addr == - 1 || $this->ip_addr === false )
        {
            trigger_error( Request::INCORRECT_IP, 256 );
        } 
        if ( ini_get( 'register_globals' ) == '1' || strtolower( ini_get( 'register_globals' ) ) == 'on' ) $this->is_register_globals = true;
        if ( function_exists( 'get_magic_quotes_gpc' ) )
        {
            if ( get_magic_quotes_gpc() ) $this->is_magic_quotes_gpc = true;
        }
        if ( PHP_VERSION >= 5.2 && extension_loaded( 'filter' ) && filter_id( ini_get( 'filter.default' ) ) !== FILTER_UNSAFE_RAW ) $this->is_filter = true;
 
        $_REQUEST = array_merge( $_POST, array_diff_key( $_GET, $_POST ) );
    }
 
 

    /**
     * Request::unhtmlentities()
     * 
     * @param mixed $value
     * @return
     */
    private function unhtmlentities ( $value )
    {
        $value = preg_replace( "/%3A%2F%2F/", '', $value );
        $value = preg_replace( '/([\x00-\x08][\x0b-\x0c][\x0e-\x20])/', '', $value );
        $value = preg_replace( "/%u0([a-z0-9]{3})/i", "&#x\\1;", $value );
        $value = preg_replace( "/%([a-z0-9]{2})/i", "&#x\\1;", $value );
        $value = str_ireplace( array( 
            '&#x53;&#x43;&#x52;&#x49;&#x50;&#x54;', '&#x26;&#x23;&#x78;&#x36;&#x41;&#x3B;&#x26;&#x23;&#x78;&#x36;&#x31;&#x3B;&#x26;&#x23;&#x78;&#x37;&#x36;&#x3B;&#x26;&#x23;&#x78;&#x36;&#x31;&#x3B;&#x26;&#x23;&#x78;&#x37;&#x33;&#x3B;&#x26;&#x23;&#x78;&#x36;&#x33;&#x3B;&#x26;&#x23;&#x78;&#x37;&#x32;&#x3B;&#x26;&#x23;&#x78;&#x36;&#x39;&#x3B;&#x26;&#x23;&#x78;&#x37;&#x30;&#x3B;&#x26;&#x23;&#x78;&#x37;&#x34;&#x3B;', '/*', '*/', '<!--', '-->', '<!-- -->', '&#x0A;', '&#x0D;', '&#x09;', '' 
        ), '', $value );
        $search = '/&#[xX]0{0,8}(21|22|23|24|25|26|27|28|29|2a|2b|2d|2f|30|31|32|33|34|35|36|37|38|39|3a|3b|3d|3f|40|41|42|43|44|45|46|47|48|49|4a|4b|4c|4d|4e|4f|50|51|52|53|54|55|56|57|58|59|5a|5b|5c|5d|5e|5f|60|61|62|63|64|65|66|67|68|69|6a|6b|6c|6d|6e|6f|70|71|72|73|74|75|76|77|78|79|7a|7b|7c|7d|7e);?/ie';
        $value = preg_replace( $search, "chr(hexdec('\\1'))", $value );
        $search = '/&#0{0,8}(33|34|35|36|37|38|39|40|41|42|43|45|47|48|49|50|51|52|53|54|55|56|57|58|59|61|63|64|65|66|67|68|69|70|71|72|73|74|75|76|77|78|79|80|81|82|83|84|85|86|87|88|89|90|91|92|93|94|95|96|97|98|99|100|101|102|103|104|105|106|107|108|109|110|111|112|113|114|115|116|117|118|119|120|121|122|123|124|125|126);?/ie';
        $value = preg_replace( $search, "chr('\\1')", $value );
        $search = array( 
            '&#60', '&#060', '&#0060', '&#00060', '&#000060', '&#0000060', '&#60;', '&#060;', '&#0060;', '&#00060;', '&#000060;', '&#0000060;', '&#x3c', '&#x03c', '&#x003c', '&#x0003c', '&#x00003c', '&#x000003c', '&#x3c;', '&#x03c;', '&#x003c;', '&#x0003c;', '&#x00003c;', '&#x000003c;', '&#X3c', '&#X03c', '&#X003c', '&#X0003c', '&#X00003c', '&#X000003c', '&#X3c;', '&#X03c;', '&#X003c;', '&#X0003c;', '&#X00003c;', '&#X000003c;', '&#x3C', '&#x03C', '&#x003C', '&#x0003C', '&#x00003C', '&#x000003C', '&#x3C;', '&#x03C;', '&#x003C;', '&#x0003C;', '&#x00003C;', '&#x000003C;', '&#X3C', '&#X03C', '&#X003C', '&#X0003C', '&#X00003C', '&#X000003C', '&#X3C;', '&#X03C;', '&#X003C;', '&#X0003C;', '&#X00003C;', '&#X000003C;', '\x3c', '\x3C', '\u003c', '\u003C' 
        );
        $value = str_ireplace( $search, '<', $value );
        return $value;
    }

    /**
     * Request::filterAttr()
     * 
     * @param mixed $attrSet
     * @return
     */
    private function filterAttr ( $attrSet )
    {
        $newSet = array();
        for ( $i = 0; $i < count( $attrSet ); $i ++ )
        {
            if ( ! $attrSet[$i] ) continue;
            $attrSubSet = array_map( "trim", explode( '=', trim( $attrSet[$i] ), 2 ) );
            $attrSubSet[0] = strtolower( $attrSubSet[0] );
            if ( ! preg_match( "/[a-z]+/i", $attrSubSet[0] ) || in_array( $attrSubSet[0], $this->disabledattributes ) || preg_match( "/^on/i", $attrSubSet[0] ) ) continue;
            if ( ! empty( $attrSubSet[1] ) )
            {
                $attrSubSet[1] = preg_replace( '/\s+/', ' ', $attrSubSet[1] );
                $attrSubSet[1] = preg_replace( "/^\"(.*)\"$/", "\\1", $attrSubSet[1] );
                $attrSubSet[1] = preg_replace( "/^\'(.*)\'$/", "\\1", $attrSubSet[1] );
                $attrSubSet[1] = str_replace( array( 
                    '"', '&quot;' 
                ), "'", $attrSubSet[1] );
                if ( preg_match( "/(expression|javascript|behaviour|vbscript|mocha|livescript)(\:*)/", $attrSubSet[1] ) ) continue;
                if ( ! empty( $this->disablecomannds ) and preg_match( '#(' . implode( '|', $this->disablecomannds ) . ')(\s*)\((.*?)\)#si', $attrSubSet[1] ) ) continue;
                $value = $this->unhtmlentities( $attrSubSet[1] );
                $search = array( 
                    'javascript' => '/j\s*a\s*v\s*a\s*s\s*c\s*r\s*i\s*p\s*t/si', 'vbscript' => '/v\s*b\s*s\s*c\s*r\s*i\s*p\s*t/si', 'script' => '/s\s*c\s*r\s*i\s*p\s*t/si', 'applet' => '/a\s*p\s*p\s*l\s*e\s*t/si', 'alert' => '/a\s*l\s*e\s*r\s*t/si', 'document' => '/d\s*o\s*c\s*u\s*m\s*e\s*n\s*t/si', 'write' => '/w\s*r\s*i\s*t\s*e/si', 'cookie' => '/c\s*o\s*o\s*k\s*i\s*e/si', 'window' => '/w\s*i\s*n\s*d\s*o\s*w/si' 
                );
                $value = preg_replace( array_values( $search ), array_keys( $search ), $value );
                if ( preg_match( "/(expression|javascript|behaviour|vbscript|mocha|livescript)(\:*)/", $value ) ) continue;
                if ( ! empty( $this->disablecomannds ) and preg_match( '#(' . implode( '|', $this->disablecomannds ) . ')(\s*)\((.*?)\)#si', $value ) ) continue;
                $attrSubSet[1] = preg_replace_callback( "/\#([0-9ABCDEFabcdef]{3,6})[\;]*/", 'color_hex2rgb', $attrSubSet[1] );
            }
            elseif ( $attrSubSet[1] !== "0" )
            {
                $attrSubSet[1] = $attrSubSet[0];
            }
            $newSet[] = $attrSubSet[0] . '=[:' . $attrSubSet[1] . ':]';
        }
        return $newSet;
    }

    /**
     * Request::filterTags()
     * 
     * @param mixed $source
     * @return
     */
    private function filterTags ( $source )
    {
        $preTag = null;
        $postTag = $source;
        $tagOpen_start = strpos( $source, '<' );
        while ( $tagOpen_start !== false )
        {
            $preTag .= substr( $postTag, 0, $tagOpen_start );
            $postTag = substr( $postTag, $tagOpen_start );
            $fromTagOpen = substr( $postTag, 1 );
            $tagOpen_end = strpos( $fromTagOpen, '>' );
            if ( $tagOpen_end === false ) break;
            $tagOpen_nested = strpos( $fromTagOpen, '<' );
            if ( ( $tagOpen_nested !== false ) && ( $tagOpen_nested < $tagOpen_end ) )
            {
                $preTag .= substr( $postTag, 0, ( $tagOpen_nested + 1 ) );
                $postTag = substr( $postTag, ( $tagOpen_nested + 1 ) );
                $tagOpen_start = strpos( $postTag, '<' );
                continue;
            }
            $tagOpen_nested = ( strpos( $fromTagOpen, '<' ) + $tagOpen_start + 1 );
            $currentTag = substr( $fromTagOpen, 0, $tagOpen_end );
            $tagLength = strlen( $currentTag );
            if ( ! $tagOpen_end )
            {
                $preTag .= $postTag;
                $tagOpen_start = strpos( $postTag, '<' );
            }
            $tagLeft = $currentTag;
            $attrSet = array();
            $currentSpace = strpos( $tagLeft, ' ' );
            if ( substr( $currentTag, 0, 1 ) == "/" )
            {
                $isCloseTag = true;
                list( $tagName ) = explode( ' ', $currentTag );
                $tagName = strtolower( substr( $tagName, 1 ) );
            }
            else
            {
                $isCloseTag = false;
                list( $tagName ) = explode( ' ', $currentTag );
                $tagName = strtolower( $tagName );
            }
            if ( ( ! preg_match( "/^[a-z][a-z0-9]*$/i", $tagName ) ) || in_array( $tagName, $this->disabletags ) )
            {
                $postTag = substr( $postTag, ( $tagLength + 2 ) );
                $tagOpen_start = strpos( $postTag, '<' );
                continue;
            }
            while ( $currentSpace !== false )
            {
                $fromSpace = substr( $tagLeft, ( $currentSpace + 1 ) );
                $nextSpace = strpos( $fromSpace, ' ' );
                $openQuotes = strpos( $fromSpace, '"' );
                $closeQuotes = strpos( substr( $fromSpace, ( $openQuotes + 1 ) ), '"' ) + $openQuotes + 1;
                if ( strpos( $fromSpace, '=' ) !== false )
                {
                    if ( ( $openQuotes !== false ) && ( strpos( substr( $fromSpace, ( $openQuotes + 1 ) ), '"' ) !== false ) ) $attr = substr( $fromSpace, 0, ( $closeQuotes + 1 ) );
                    else $attr = substr( $fromSpace, 0, $nextSpace );
                }
                else
                    $attr = substr( $fromSpace, 0, $nextSpace );
                if ( ! $attr ) $attr = $fromSpace;
                $attrSet[] = $attr;
                $tagLeft = substr( $fromSpace, strlen( $attr ) );
                $currentSpace = strpos( $tagLeft, ' ' );
            }
            if ( ! $isCloseTag )
            {
                $preTag .= '{:' . $tagName;
                if ( ! empty( $attrSet ) )
                {
                    $attrSet = $this->filterAttr( $attrSet );
                    $preTag .= ' ' . implode( ' ', $attrSet );
                }
                $preTag .= ( strpos( $fromTagOpen, "</" . $tagName ) ) ? ':}' : ' /:}';
            }
            else
            {
                $preTag .= '{:/' . $tagName . ':}';
            }
            $postTag = substr( $postTag, ( $tagLength + 2 ) );
            $tagOpen_start = strpos( $postTag, '<' );
        }
        $preTag .= $postTag;
        return $preTag;
    } 

    /**
     * Request::security_post()
     * 
     * @param mixed $value
     * @return
     */
    private function security_post ( $value )
    {
        if ( is_array( $value ) )
        {
            $keys = array_keys( $value );
            foreach ( $keys as $key )
            {
                $value[$key] = $this->security_post( $value[$key] );
            }
        }
        else
        {
            $value = preg_replace( "/\t+/", " ", $value );
            unset( $matches );
            preg_match_all( '/<!\[cdata\[(.*?)\]\]>/is', $value, $matches );
            $value = str_replace( $matches[0], $matches[1], $value );
            $value = $this->filterTags( $value );
            $value = str_replace( array( 
                "'", '"', '<', '>' 
            ), array( 
                "&#039;", "&quot;", "&lt;", "&gt;" 
            ), $value );
            $value = str_replace( array( 
                "[:", ":]", "{:", ":}" 
            ), array( 
                '"', '"', "<", '>' 
            ), $value );
            $value = trim( $value );
        }
        return $value;
    }

  

    /**
     * Request::parse_mode()
     * 
     * @param mixed $mode
     * @return
     */
    private function parse_mode ( $mode )
    {
        if ( empty( $mode ) ) return array( 
            $this->request_default_mode 
        );
        $mode = explode( ",", $mode );
        $mode = array_map( 'trim', $mode );
        $mode = array_map( 'strtolower', $mode );
        $mode = array_intersect( $this->allow_request_mods, $mode );
        if ( empty( $mode ) ) return array( 
            $this->request_default_mode 
        );
        return array_values( $mode );
    }  

    /**
     * Request::get_value()
     * 
     * @param mixed $name
     * @param mixed $mode
     * @param mixed $default
     * @param bool $decode
     * @return
     */
    private function get_value ( $name, $mode = null, $default = null, $decode = true )
    {
        $modes = $this->parse_mode( $mode );
        foreach ( $modes as $mode )
        {
            switch ( $mode )
            {
                case 'get':
                    if ( array_key_exists( $name, $_GET ) )
                    {
                        $value = $_GET[$name];
                        return $value;
                    }
                    break;
                case 'post':
                    if ( array_key_exists( $name, $_POST ) )
                    {
                        $value = $_POST[$name];
                        if ( empty( $value ) or is_numeric( $value ) ) return $value;
                        return $this->security_post( $value );
                    }
                    break;
                case 'request':
                    if ( array_key_exists( $name, $_POST ) )
                    {
                        $value = $_POST[$name];
                        if ( empty( $value ) or is_numeric( $value ) ) return $value;
                        return $this->security_post( $value );
                    }
                    elseif ( array_key_exists( $name, $_GET ) )
                    {
                        $value = $_GET[$name];
                        return $value;
                    }
                    break; 
            }
        }
        return $default;
    }
 

    /**
     * Request::get_bool()
     * 
     * @param mixed $name
     * @param mixed $mode
     * @param mixed $default
     * @param bool $decode
     * @return
     */
    public function get_bool ( $name, $mode = null, $default = null, $decode = true )
    {
        return ( bool )$this->get_value( $name, $mode, $default, $decode );
    }

    /**
     * Request::get_int()
     * 
     * @param mixed $name
     * @param mixed $mode
     * @param mixed $default
     * @param bool $decode
     * @return
     */
    public function get_int ( $name, $mode = null, $default = null, $decode = true )
    {
        return ( int )$this->get_value( $name, $mode, $default, $decode );
    }

    /**
     * Request::get_float()
     * 
     * @param mixed $name
     * @param mixed $mode
     * @param mixed $default
     * @param bool $decode
     * @return
     */
    public function get_float ( $name, $mode = null, $default = null, $decode = true )
    {
        return ( float )$this->get_value( $name, $mode, $default, $decode );
    }

    /**
     * Request::get_string()
     * 
     * @param mixed $name
     * @param mixed $mode
     * @param mixed $default
     * @param bool $decode
     * @return
     */
    public function get_string ( $name, $mode = null, $default = null, $decode = true )
    {
        return ( string )$this->get_value( $name, $mode, $default, $decode );
    }

    /**
     * Request::get_array()
     * 
     * @param mixed $name
     * @param mixed $mode
     * @param mixed $default
     * @param bool $decode
     * @return
     */
    public function get_array ( $name, $mode = null, $default = null, $decode = true )
    {
        return ( array )$this->get_value( $name, $mode, $default, $decode );
    } 
} 
$_B['r'] = new Request(); 

?>