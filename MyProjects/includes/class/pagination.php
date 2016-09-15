<?php
class Pagination {
    private $max, $total, $parameter, $start = 0;

    private $i      = 0;
    private $href   = '';
    var $pagination = array();
    var $totalPage;

    function __construct($href, $max, $total, $max_items = 10, $parameter = 'p') { 
        $this->max       = $max;
        $this->total     = $total;
        $this->parameter = $parameter;
        $this->max_items = $max_items;
        $this->get       = (!empty($_GET[$this->parameter]) && ($_GET[$this->parameter] <= $this->pages())) ? $_GET[$this->parameter] : 1;
       
       
        //Xoa bo ?
       
        // if(isset($_COOKIE['truong'])){
        //     echo '<pre>';
        //     print_r($_GET);
        //     echo '</pre>';
        //     die();
        // }
        // echo $hrefFirst;
        // $re = "/-p[0-9]+/";
        // if($this->get==1){
        //     if (preg_match($re,$hrefFirst)) {
        //         $hrefFirst = preg_replace($re, '-'.$parameter.'{nbh}', $hrefFirst);
        //     }else{
        //         $hrefFirst = str_replace($web['dotExtension'],"",$hrefFirst);
        //     }
        // }else{
        //     if (preg_match($re,$hrefFirst)) {
        //         $hrefFirst = preg_replace($re, '-'.$parameter.'{nbh}', $hrefFirst);
        //     }
        // }
        // echo '<br/>'.$hrefFirst;
        // $hrefFirst .= $web['dotExtension'];
        $this->pagination = array(
            'first'    => $this->first($href),
            'previous' => $this->previous($href),
            'numbers'  => $this->numbers($href),
            'next'     => $this->next($href),
            'last'     => $this->last($href),
            'info'     => $this->info(),
        );
    }

    function start() {
        $start = $this->get - 1;
        $calc  = $start * $this->max;
        return $calc;
    }
    function end() {
        $calc = $this->start() + $this->max;
        $r    = ($calc > $this->total) ? $this->total : $calc;
        return $r;
    }

    function pages() {
        return @ceil($this->total / $this->max);
    }
    function info() {
        $code = array(
            'total' => $this->total,
            'start' => $this->start() + 1,
            'end'   => $this->end(),
            'page'  => $this->get,
            'pages' => $this->pages(),
        );
        return $code;
    }
    function gettiento($href){
        if (strpos($href,'?') !== false) {
            return $href.'&';
        } else {
            return $href.'?';
        }
    }
    function first($href) {
        if ($this->get != 1) {
            $nbh['num']  = 1;
            $nbh['href'] = $this->gettiento($href).'p='.$nbh['num'];//str_replace('{nbh}', $nbh['num'], $href);
            return $nbh;
        }

    }

    function previous($href) {
        if ($this->get != 1) {
            $nbh['num']  = $this->get - 1;
            $nbh['href'] = $this->gettiento($href).'p='.$nbh['num'];//str_replace('{nbh}', $nbh['num'], $href);
            return $nbh;
        }

    }

    function next($href) {
        if ($this->get < $this->pages()) {
            $nbh['num']  = $this->get + 1;
            $nbh['href'] = $this->gettiento($href).'p='.$nbh['num'];//str_replace('{nbh}', $nbh['num'], $href);
            return $nbh;
        }

    }

    function last($href) {
        if ($this->get < $this->pages()) {
            $nbh['num']  = $this->pages();
            $nbh['href'] = $this->gettiento($href).'p='.$nbh['num'];//str_replace('{nbh}', $nbh['num'], $href);
            return $nbh;
        }

    }
    function numbers($href, $reversed = false) {
        $r     = '';
        $range = floor(($this->max_items - 1) / 2);
        if (!$this->max_items) {
            $page_nums = range(1, $this->pages());
        } else {
            $lower_limit = max($this->get - $range, 1);
            $upper_limit = min($this->pages(), $this->get + $range);
            $page_nums   = range($lower_limit, $upper_limit);
        }

        if ($reversed) {
            $page_nums = array_reverse($page_nums);
        }

        foreach ($page_nums as $k => $i) {
            if ($this->get == $i) {
                $nbh['active']['num'] = $i;
            } else {
                $nbh[$k + 1]['num']  = $i;
                $nbh[$k + 1]['href'] = $this->gettiento($href).'p='. $nbh[$k + 1]['num'];//str_replace('{nbh}', $nbh['num'], $href);
            }
        }
        return $nbh;
    }

    function paginator() {
        $this->i = $this->i + 1;
        if ($this->i > $this->start() && $this->i <= $this->end()) {
            return true;
        }
    }
}
