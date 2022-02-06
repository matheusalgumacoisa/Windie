--
-- PostgreSQL database dump
--

-- Dumped from database version 13.3
-- Dumped by pg_dump version 13.3

-- Started on 2022-02-06 20:17:11

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 230 (class 1255 OID 16599)
-- Name: bytea_import(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.bytea_import(p_path text) RETURNS bytea
    LANGUAGE plpgsql
    AS $$
declare
  l_oid oid;
  p_result bytea;
begin
  select lo_import(p_path) into l_oid;
  select lo_get(l_oid) INTO p_result;
  perform lo_unlink(l_oid);
  return p_result;
end;$$;


ALTER FUNCTION public.bytea_import(p_path text) OWNER TO postgres;

--
-- TOC entry 243 (class 1255 OID 16734)
-- Name: proc_atualizar_aprovacao(); Type: PROCEDURE; Schema: public; Owner: postgres
--

CREATE PROCEDURE public.proc_atualizar_aprovacao()
    LANGUAGE plpgsql
    AS $$
declare 
  jogos_sem_ficha
  cursor for
  SELECT jg.jogo_id
	FROM jogo jg
	full outer JOIN ficha_aprovacao fa 
	ON jg.jogo_id = fa.jogo_id
	WHERE jg.visibilidade = 'PUBLICO' AND fa.ficha_aprovacao_id is null;
 begin
 for jogo_sem_ficha in jogos_sem_ficha
		loop
			insert into ficha_aprovacao (data_inicio,estado,jogo_id) values (CURRENT_DATE,'ABERTO',jogo_sem_ficha.jogo_id);--cria fichas em aberto para jogos não aprovados
		end loop;
		
	  update ficha_aprovacao fa
	  set estado = 'APROVADO'
	  where CURRENT_DATE - 30 > fa.data_inicio and fa.estado = 'ABERTO' --precisa ter passado 30 dias da data de inicio e ainda estar em aberto
	  	and (((100*(select count(*) vot from voto v where v.ficha_aprovacao_id = fa.ficha_aprovacao_id and se_favoravel = 'S'))--regra de 3
  		/(select count(*) vot from voto v where v.ficha_aprovacao_id = fa.ficha_aprovacao_id) >=80)--caso os votos pósitivos sejam mais de 80%
		and ((select count(*) vot from voto v where v.ficha_aprovacao_id = fa.ficha_aprovacao_id)>100--e caso haja mais de 100 votos 
			 or (select count(*) vot from voto v where v.ficha_aprovacao_id = fa.ficha_aprovacao_id) 
			 > (select count(*) ass from view_usuario_assinatura where assinatura_status = 'VALIDA' )*0.25));--ou caso a quantidade de votos seja 25% maior que o total de assinantes
			 
			 
			 
			 
	  update ficha_aprovacao fa
	  set estado = 'REPROVADO'
	  where CURRENT_DATE - 30 > fa.data_inicio and fa.estado = 'ABERTO'
	  	and (((100*(select count(*) vot from voto v where v.ficha_aprovacao_id = fa.ficha_aprovacao_id and se_favoravel = 'S'))--regra de 3
  		/(select count(*) vot from voto v where v.ficha_aprovacao_id = fa.ficha_aprovacao_id) <80)--caso os votos pósitivos sejam menos de 80%
		and ((select count(*) vot from voto v where v.ficha_aprovacao_id = fa.ficha_aprovacao_id)>100--e caso haja mais de 100 votos 
			 or (select count(*) vot from voto v where v.ficha_aprovacao_id = fa.ficha_aprovacao_id) 
			 > (select count(*) ass from view_usuario_assinatura where assinatura_status = 'VALIDA' )*0.25));--ou caso a quantidade de votos seja 25% maior que o total de assinantes
end;
$$;


ALTER PROCEDURE public.proc_atualizar_aprovacao() OWNER TO postgres;

--
-- TOC entry 242 (class 1255 OID 33252)
-- Name: proc_atualizar_aprovacao_teste(); Type: PROCEDURE; Schema: public; Owner: postgres
--

CREATE PROCEDURE public.proc_atualizar_aprovacao_teste()
    LANGUAGE plpgsql
    AS $$
declare 
  jogos_sem_ficha
  cursor for
  SELECT jg.jogo_id
	FROM jogo jg
	full outer JOIN ficha_aprovacao fa 
	ON jg.jogo_id = fa.jogo_id
	WHERE jg.visibilidade = 'PUBLICO' AND fa.ficha_aprovacao_id is null;
 begin
 for jogo_sem_ficha in jogos_sem_ficha
		loop
			insert into ficha_aprovacao (data_inicio,estado,jogo_id) values (CURRENT_DATE,'ABERTO',jogo_sem_ficha.jogo_id);--cria fichas em aberto para jogos não aprovados
		end loop;
		
	  update ficha_aprovacao fa
	  set estado = 'APROVADO'
	  where CURRENT_DATE - 0 >= fa.data_inicio and fa.estado = 'ABERTO' --não precisa ter passado 30 dias da data de inicio e ainda estar em aberto
	  	and (((100*(select count(*) vot from voto v where v.ficha_aprovacao_id = fa.ficha_aprovacao_id and se_favoravel = 'S'))--regra de 3
  		/(select count(*) vot from voto v where v.ficha_aprovacao_id = fa.ficha_aprovacao_id) >=80)--caso os votos pósitivos sejam mais de 80%
		and ((select count(*) vot from voto v where v.ficha_aprovacao_id = fa.ficha_aprovacao_id)>=2--e caso haja mais de 2 votos 
			 or (select count(*) vot from voto v where v.ficha_aprovacao_id = fa.ficha_aprovacao_id) 
			 > (select count(*) ass from view_usuario_assinatura where assinatura_status = 'VALIDA' )*0.25));--ou caso a quantidade de votos seja 25% maior que o total de assinantes
			 
			 
			 
			 
	  update ficha_aprovacao fa
	  set estado = 'REPROVADO'
	  where CURRENT_DATE - 0 >= fa.data_inicio and fa.estado = 'ABERTO'
	  	and (((100*(select count(*) vot from voto v where v.ficha_aprovacao_id = fa.ficha_aprovacao_id and se_favoravel = 'S'))--regra de 3
  		/(select count(*) vot from voto v where v.ficha_aprovacao_id = fa.ficha_aprovacao_id) <80)--caso os votos pósitivos sejam menos de 80%
		and ((select count(*) vot from voto v where v.ficha_aprovacao_id = fa.ficha_aprovacao_id)>=2--e caso haja mais de 2 votos 
			 or (select count(*) vot from voto v where v.ficha_aprovacao_id = fa.ficha_aprovacao_id) 
			 > (select count(*) ass from view_usuario_assinatura where assinatura_status = 'VALIDA' )*0.25));--ou caso a quantidade de votos seja 25% maior que o total de assinantes
end;
$$;


ALTER PROCEDURE public.proc_atualizar_aprovacao_teste() OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 201 (class 1259 OID 16397)
-- Name: avaliacao; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.avaliacao (
    avaliacao_id integer NOT NULL,
    nota integer NOT NULL,
    jogo_id integer NOT NULL,
    usuario_id integer NOT NULL
);


ALTER TABLE public.avaliacao OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 16395)
-- Name: avaliacao_avaliacao_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.avaliacao_avaliacao_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.avaliacao_avaliacao_id_seq OWNER TO postgres;

--
-- TOC entry 3149 (class 0 OID 0)
-- Dependencies: 200
-- Name: avaliacao_avaliacao_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.avaliacao_avaliacao_id_seq OWNED BY public.avaliacao.avaliacao_id;


--
-- TOC entry 203 (class 1259 OID 16405)
-- Name: desenvolvedor; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.desenvolvedor (
    desenvolvedor_id integer NOT NULL,
    nome_de_desenvolvedor character varying(100) NOT NULL,
    usuario_id integer NOT NULL,
    email_paypal character varying(1000) NOT NULL
);


ALTER TABLE public.desenvolvedor OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 16403)
-- Name: desenvolvedor_desenvolvedor_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.desenvolvedor_desenvolvedor_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.desenvolvedor_desenvolvedor_id_seq OWNER TO postgres;

--
-- TOC entry 3150 (class 0 OID 0)
-- Dependencies: 202
-- Name: desenvolvedor_desenvolvedor_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.desenvolvedor_desenvolvedor_id_seq OWNED BY public.desenvolvedor.desenvolvedor_id;


--
-- TOC entry 205 (class 1259 OID 16413)
-- Name: ficha_aprovacao; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ficha_aprovacao (
    ficha_aprovacao_id integer NOT NULL,
    data_inicio date NOT NULL,
    estado character varying(10) NOT NULL,
    jogo_id integer NOT NULL,
    CONSTRAINT estado_pattern CHECK ((((estado)::text = 'APROVADO'::text) OR ((estado)::text = 'REPROVADO'::text) OR ((estado)::text = 'ABERTO'::text)))
);


ALTER TABLE public.ficha_aprovacao OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 16411)
-- Name: ficha_aprovacao_ficha_aprovacao_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.ficha_aprovacao_ficha_aprovacao_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ficha_aprovacao_ficha_aprovacao_id_seq OWNER TO postgres;

--
-- TOC entry 3151 (class 0 OID 0)
-- Dependencies: 204
-- Name: ficha_aprovacao_ficha_aprovacao_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.ficha_aprovacao_ficha_aprovacao_id_seq OWNED BY public.ficha_aprovacao.ficha_aprovacao_id;


--
-- TOC entry 219 (class 1259 OID 16520)
-- Name: genero; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.genero (
    genero_id integer NOT NULL,
    genero_nome character varying(100)
);


ALTER TABLE public.genero OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 16518)
-- Name: genero_genero_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.genero_genero_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.genero_genero_id_seq OWNER TO postgres;

--
-- TOC entry 3152 (class 0 OID 0)
-- Dependencies: 218
-- Name: genero_genero_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.genero_genero_id_seq OWNED BY public.genero.genero_id;


--
-- TOC entry 207 (class 1259 OID 16421)
-- Name: horas_jogadas_mes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.horas_jogadas_mes (
    horas numeric(800,2) NOT NULL,
    mes_referente date NOT NULL,
    jogo_id integer NOT NULL,
    horas_jogadas_mes_id integer NOT NULL
);


ALTER TABLE public.horas_jogadas_mes OWNER TO postgres;

--
-- TOC entry 206 (class 1259 OID 16419)
-- Name: horas_jogadas_mes_horas_jogadas_mes_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.horas_jogadas_mes_horas_jogadas_mes_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.horas_jogadas_mes_horas_jogadas_mes_id_seq OWNER TO postgres;

--
-- TOC entry 3153 (class 0 OID 0)
-- Dependencies: 206
-- Name: horas_jogadas_mes_horas_jogadas_mes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.horas_jogadas_mes_horas_jogadas_mes_id_seq OWNED BY public.horas_jogadas_mes.horas_jogadas_mes_id;


--
-- TOC entry 209 (class 1259 OID 16429)
-- Name: item_biblioteca; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.item_biblioteca (
    horas_jogadas numeric(1000,2) NOT NULL,
    jogo_id integer NOT NULL,
    usuario_id integer NOT NULL,
    item_biblioteca_id integer NOT NULL
);


ALTER TABLE public.item_biblioteca OWNER TO postgres;

--
-- TOC entry 208 (class 1259 OID 16427)
-- Name: item_biblioteca_item_biblioteca_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.item_biblioteca_item_biblioteca_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.item_biblioteca_item_biblioteca_id_seq OWNER TO postgres;

--
-- TOC entry 3154 (class 0 OID 0)
-- Dependencies: 208
-- Name: item_biblioteca_item_biblioteca_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.item_biblioteca_item_biblioteca_id_seq OWNED BY public.item_biblioteca.item_biblioteca_id;


--
-- TOC entry 211 (class 1259 OID 16437)
-- Name: jogo; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.jogo (
    jogo_id integer NOT NULL,
    titulo character varying(100),
    descricao character varying(300),
    caminho_executavel character varying(500),
    detalhes character varying(5000),
    tags character varying(200),
    visibilidade character varying(10) NOT NULL,
    imagem_capa bytea,
    genero integer,
    desenvolvedor_id integer NOT NULL,
    arquivo_caminho character varying(500)
);


ALTER TABLE public.jogo OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 16435)
-- Name: jogo_jogo_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.jogo_jogo_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.jogo_jogo_id_seq OWNER TO postgres;

--
-- TOC entry 3155 (class 0 OID 0)
-- Dependencies: 210
-- Name: jogo_jogo_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.jogo_jogo_id_seq OWNED BY public.jogo.jogo_id;


--
-- TOC entry 213 (class 1259 OID 16448)
-- Name: pagamento_assinatura; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pagamento_assinatura (
    pagamento_id integer NOT NULL,
    data_pagamento date,
    valor numeric(99,2) NOT NULL,
    mes_referente date NOT NULL,
    usuario_id integer NOT NULL,
    checkout_key character varying(1000)
);


ALTER TABLE public.pagamento_assinatura OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 16446)
-- Name: pagamento_assinatura_pagamento_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pagamento_assinatura_pagamento_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.pagamento_assinatura_pagamento_id_seq OWNER TO postgres;

--
-- TOC entry 3156 (class 0 OID 0)
-- Dependencies: 212
-- Name: pagamento_assinatura_pagamento_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pagamento_assinatura_pagamento_id_seq OWNED BY public.pagamento_assinatura.pagamento_id;


--
-- TOC entry 223 (class 1259 OID 16571)
-- Name: pagamento_desenvolvedor; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pagamento_desenvolvedor (
    pagamento_desenvolvedor_id integer NOT NULL,
    data date,
    valor double precision,
    mes_referente date,
    desenvolvedor_id integer NOT NULL,
    pag_batch_id integer
);


ALTER TABLE public.pagamento_desenvolvedor OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 16741)
-- Name: pagamento_desenvolvedor_pag_batch_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pagamento_desenvolvedor_pag_batch_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.pagamento_desenvolvedor_pag_batch_id_seq OWNER TO postgres;

--
-- TOC entry 3157 (class 0 OID 0)
-- Dependencies: 228
-- Name: pagamento_desenvolvedor_pag_batch_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pagamento_desenvolvedor_pag_batch_id_seq OWNED BY public.pagamento_desenvolvedor.pag_batch_id;


--
-- TOC entry 222 (class 1259 OID 16569)
-- Name: pagamento_desenvolvedor_pagamento_desenvolvedor_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pagamento_desenvolvedor_pagamento_desenvolvedor_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.pagamento_desenvolvedor_pagamento_desenvolvedor_id_seq OWNER TO postgres;

--
-- TOC entry 3158 (class 0 OID 0)
-- Dependencies: 222
-- Name: pagamento_desenvolvedor_pagamento_desenvolvedor_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pagamento_desenvolvedor_pagamento_desenvolvedor_id_seq OWNED BY public.pagamento_desenvolvedor.pagamento_desenvolvedor_id;


--
-- TOC entry 215 (class 1259 OID 16456)
-- Name: screenshot; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.screenshot (
    screenshot_id integer NOT NULL,
    imagem bytea NOT NULL,
    jogo_id integer NOT NULL
);


ALTER TABLE public.screenshot OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 16454)
-- Name: screenshot_screenshot_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.screenshot_screenshot_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.screenshot_screenshot_id_seq OWNER TO postgres;

--
-- TOC entry 3159 (class 0 OID 0)
-- Dependencies: 214
-- Name: screenshot_screenshot_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.screenshot_screenshot_id_seq OWNED BY public.screenshot.screenshot_id;


--
-- TOC entry 217 (class 1259 OID 16467)
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuario (
    usuario_id integer NOT NULL,
    email character varying(100) NOT NULL,
    senha character varying(100) NOT NULL,
    apelido character varying(20) NOT NULL
);


ALTER TABLE public.usuario OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 16465)
-- Name: usuario_usuario_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.usuario_usuario_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.usuario_usuario_id_seq OWNER TO postgres;

--
-- TOC entry 3160 (class 0 OID 0)
-- Dependencies: 216
-- Name: usuario_usuario_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.usuario_usuario_id_seq OWNED BY public.usuario.usuario_id;


--
-- TOC entry 227 (class 1259 OID 16725)
-- Name: view_jogo_aprovacao; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.view_jogo_aprovacao AS
 SELECT jg.jogo_id,
    jg.titulo,
    jg.descricao,
    jg.caminho_executavel,
    jg.detalhes,
    jg.tags,
    jg.visibilidade,
    jg.imagem_capa,
    jg.genero,
    jg.desenvolvedor_id
   FROM (public.jogo jg
     FULL JOIN public.ficha_aprovacao fa ON ((jg.jogo_id = fa.jogo_id)))
  WHERE (((jg.visibilidade)::text = 'PUBLICO'::text) AND (((fa.estado)::text = 'ABERTO'::text) OR (fa.ficha_aprovacao_id IS NULL)));


ALTER TABLE public.view_jogo_aprovacao OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 16754)
-- Name: view_jogo_biblioteca; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.view_jogo_biblioteca AS
 SELECT jg.jogo_id,
    jg.titulo,
    jg.descricao,
    jg.caminho_executavel,
    jg.detalhes,
    jg.tags,
    jg.visibilidade,
    jg.imagem_capa,
    jg.genero,
    jg.desenvolvedor_id,
    ib.usuario_id,
    ib.horas_jogadas
   FROM (public.jogo jg
     JOIN public.item_biblioteca ib ON ((jg.jogo_id = ib.jogo_id)))
  WHERE ((jg.visibilidade)::text = 'PUBLICO'::text);


ALTER TABLE public.view_jogo_biblioteca OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 16719)
-- Name: view_jogo_catalogo; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.view_jogo_catalogo AS
 SELECT jg.jogo_id,
    jg.titulo,
    jg.descricao,
    jg.caminho_executavel,
    jg.detalhes,
    jg.tags,
    jg.visibilidade,
    jg.imagem_capa,
    jg.genero,
    jg.desenvolvedor_id
   FROM (public.jogo jg
     JOIN public.ficha_aprovacao fa ON ((jg.jogo_id = fa.jogo_id)))
  WHERE (((jg.visibilidade)::text = 'PUBLICO'::text) AND ((fa.estado)::text = 'APROVADO'::text));


ALTER TABLE public.view_jogo_catalogo OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 16631)
-- Name: view_jogo_classific; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.view_jogo_classific AS
SELECT
    NULL::integer AS jogo_id,
    NULL::character varying(100) AS titulo,
    NULL::character varying(300) AS descricao,
    NULL::character varying(500) AS caminho_executavel,
    NULL::character varying(5000) AS detalhes,
    NULL::character varying(200) AS tags,
    NULL::character varying(10) AS visibilidade,
    NULL::bytea AS imagem_capa,
    NULL::integer AS genero,
    NULL::integer AS desenvolvedor_id,
    NULL::numeric AS nota,
    NULL::bigint AS avaliacoes_numero,
    NULL::numeric AS horas_jogadas;


ALTER TABLE public.view_jogo_classific OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 16636)
-- Name: view_usuario_assinatura; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.view_usuario_assinatura AS
SELECT
    NULL::integer AS usuario_id,
    NULL::character varying(100) AS email,
    NULL::character varying(20) AS apelido,
    NULL::text AS assinatura_status;


ALTER TABLE public.view_usuario_assinatura OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16545)
-- Name: voto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.voto (
    voto_id integer NOT NULL,
    se_favoravel character varying(1),
    usuario_id integer,
    ficha_aprovacao_id integer
);


ALTER TABLE public.voto OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 16543)
-- Name: voto_voto_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.voto_voto_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.voto_voto_id_seq OWNER TO postgres;

--
-- TOC entry 3161 (class 0 OID 0)
-- Dependencies: 220
-- Name: voto_voto_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.voto_voto_id_seq OWNED BY public.voto.voto_id;


--
-- TOC entry 2945 (class 2604 OID 16400)
-- Name: avaliacao avaliacao_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.avaliacao ALTER COLUMN avaliacao_id SET DEFAULT nextval('public.avaliacao_avaliacao_id_seq'::regclass);


--
-- TOC entry 2947 (class 2604 OID 16408)
-- Name: desenvolvedor desenvolvedor_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.desenvolvedor ALTER COLUMN desenvolvedor_id SET DEFAULT nextval('public.desenvolvedor_desenvolvedor_id_seq'::regclass);


--
-- TOC entry 2948 (class 2604 OID 16416)
-- Name: ficha_aprovacao ficha_aprovacao_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ficha_aprovacao ALTER COLUMN ficha_aprovacao_id SET DEFAULT nextval('public.ficha_aprovacao_ficha_aprovacao_id_seq'::regclass);


--
-- TOC entry 2960 (class 2604 OID 16523)
-- Name: genero genero_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.genero ALTER COLUMN genero_id SET DEFAULT nextval('public.genero_genero_id_seq'::regclass);


--
-- TOC entry 2950 (class 2604 OID 16424)
-- Name: horas_jogadas_mes horas_jogadas_mes_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.horas_jogadas_mes ALTER COLUMN horas_jogadas_mes_id SET DEFAULT nextval('public.horas_jogadas_mes_horas_jogadas_mes_id_seq'::regclass);


--
-- TOC entry 2952 (class 2604 OID 16432)
-- Name: item_biblioteca item_biblioteca_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item_biblioteca ALTER COLUMN item_biblioteca_id SET DEFAULT nextval('public.item_biblioteca_item_biblioteca_id_seq'::regclass);


--
-- TOC entry 2954 (class 2604 OID 16440)
-- Name: jogo jogo_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.jogo ALTER COLUMN jogo_id SET DEFAULT nextval('public.jogo_jogo_id_seq'::regclass);


--
-- TOC entry 2956 (class 2604 OID 16451)
-- Name: pagamento_assinatura pagamento_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pagamento_assinatura ALTER COLUMN pagamento_id SET DEFAULT nextval('public.pagamento_assinatura_pagamento_id_seq'::regclass);


--
-- TOC entry 2963 (class 2604 OID 16574)
-- Name: pagamento_desenvolvedor pagamento_desenvolvedor_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pagamento_desenvolvedor ALTER COLUMN pagamento_desenvolvedor_id SET DEFAULT nextval('public.pagamento_desenvolvedor_pagamento_desenvolvedor_id_seq'::regclass);


--
-- TOC entry 2958 (class 2604 OID 16459)
-- Name: screenshot screenshot_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.screenshot ALTER COLUMN screenshot_id SET DEFAULT nextval('public.screenshot_screenshot_id_seq'::regclass);


--
-- TOC entry 2959 (class 2604 OID 16470)
-- Name: usuario usuario_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario ALTER COLUMN usuario_id SET DEFAULT nextval('public.usuario_usuario_id_seq'::regclass);


--
-- TOC entry 2961 (class 2604 OID 16548)
-- Name: voto voto_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.voto ALTER COLUMN voto_id SET DEFAULT nextval('public.voto_voto_id_seq'::regclass);


--
-- TOC entry 2986 (class 2606 OID 16668)
-- Name: usuario Unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT "Unique" UNIQUE (usuario_id, email, apelido);


--
-- TOC entry 2966 (class 2606 OID 16402)
-- Name: avaliacao avaliacao_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.avaliacao
    ADD CONSTRAINT avaliacao_pk PRIMARY KEY (avaliacao_id);


--
-- TOC entry 2968 (class 2606 OID 16410)
-- Name: desenvolvedor desenvolvedor_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.desenvolvedor
    ADD CONSTRAINT desenvolvedor_pk PRIMARY KEY (desenvolvedor_id);


--
-- TOC entry 2970 (class 2606 OID 16418)
-- Name: ficha_aprovacao ficha_aprovacao_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ficha_aprovacao
    ADD CONSTRAINT ficha_aprovacao_pk PRIMARY KEY (ficha_aprovacao_id);


--
-- TOC entry 2990 (class 2606 OID 16525)
-- Name: genero genero_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.genero
    ADD CONSTRAINT genero_pk PRIMARY KEY (genero_id);


--
-- TOC entry 2974 (class 2606 OID 16426)
-- Name: horas_jogadas_mes horas_jogadas_mes_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.horas_jogadas_mes
    ADD CONSTRAINT horas_jogadas_mes_pk PRIMARY KEY (horas_jogadas_mes_id);


--
-- TOC entry 2951 (class 2606 OID 16663)
-- Name: horas_jogadas_mes horas_positivas; Type: CHECK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.horas_jogadas_mes
    ADD CONSTRAINT horas_positivas CHECK ((horas >= (0)::numeric)) NOT VALID;


--
-- TOC entry 2953 (class 2606 OID 16664)
-- Name: item_biblioteca horas_positivas; Type: CHECK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.item_biblioteca
    ADD CONSTRAINT horas_positivas CHECK ((horas_jogadas >= (0)::numeric)) NOT VALID;


--
-- TOC entry 2976 (class 2606 OID 16434)
-- Name: item_biblioteca item_biblioteca_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item_biblioteca
    ADD CONSTRAINT item_biblioteca_pk PRIMARY KEY (item_biblioteca_id);


--
-- TOC entry 2978 (class 2606 OID 16445)
-- Name: jogo jogo_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.jogo
    ADD CONSTRAINT jogo_pk PRIMARY KEY (jogo_id);


--
-- TOC entry 2946 (class 2606 OID 16661)
-- Name: avaliacao nota_valor; Type: CHECK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.avaliacao
    ADD CONSTRAINT nota_valor CHECK (((nota >= 0) AND (nota <= 5))) NOT VALID;


--
-- TOC entry 2982 (class 2606 OID 16453)
-- Name: pagamento_assinatura pagamento_assinatura_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pagamento_assinatura
    ADD CONSTRAINT pagamento_assinatura_pk PRIMARY KEY (pagamento_id);


--
-- TOC entry 2994 (class 2606 OID 16583)
-- Name: pagamento_desenvolvedor pagamento_desenvolvedor_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pagamento_desenvolvedor
    ADD CONSTRAINT pagamento_desenvolvedor_pk PRIMARY KEY (pagamento_desenvolvedor_id);


--
-- TOC entry 2984 (class 2606 OID 16464)
-- Name: screenshot screenshot_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.screenshot
    ADD CONSTRAINT screenshot_pk PRIMARY KEY (screenshot_id);


--
-- TOC entry 2962 (class 2606 OID 16669)
-- Name: voto se_favoravel_pattern; Type: CHECK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.voto
    ADD CONSTRAINT se_favoravel_pattern CHECK ((((se_favoravel)::text = 'S'::text) OR ((se_favoravel)::text = 'N'::text))) NOT VALID;


--
-- TOC entry 2980 (class 2606 OID 16659)
-- Name: jogo titulo_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.jogo
    ADD CONSTRAINT titulo_unique UNIQUE (titulo);


--
-- TOC entry 2972 (class 2606 OID 16736)
-- Name: ficha_aprovacao unique_jogo; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ficha_aprovacao
    ADD CONSTRAINT unique_jogo UNIQUE (jogo_id);


--
-- TOC entry 2988 (class 2606 OID 16472)
-- Name: usuario usuario_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pk PRIMARY KEY (usuario_id);


--
-- TOC entry 2957 (class 2606 OID 16665)
-- Name: pagamento_assinatura valo_positivo; Type: CHECK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.pagamento_assinatura
    ADD CONSTRAINT valo_positivo CHECK ((valor >= (0)::numeric)) NOT VALID;


--
-- TOC entry 2964 (class 2606 OID 16666)
-- Name: pagamento_desenvolvedor valor_positivo; Type: CHECK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.pagamento_desenvolvedor
    ADD CONSTRAINT valor_positivo CHECK ((valor >= (0)::double precision)) NOT VALID;


--
-- TOC entry 2955 (class 2606 OID 16660)
-- Name: jogo visibilidade_pattern; Type: CHECK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE public.jogo
    ADD CONSTRAINT visibilidade_pattern CHECK ((((visibilidade)::text = 'PUBLICO'::text) OR ((visibilidade)::text = 'RASCUNHO'::text))) NOT VALID;


--
-- TOC entry 2992 (class 2606 OID 16550)
-- Name: voto voto_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.voto
    ADD CONSTRAINT voto_pk PRIMARY KEY (voto_id);


--
-- TOC entry 3139 (class 2618 OID 16634)
-- Name: view_jogo_classific _RETURN; Type: RULE; Schema: public; Owner: postgres
--

CREATE OR REPLACE VIEW public.view_jogo_classific AS
 SELECT jg.jogo_id,
    jg.titulo,
    jg.descricao,
    jg.caminho_executavel,
    jg.detalhes,
    jg.tags,
    jg.visibilidade,
    jg.imagem_capa,
    jg.genero,
    jg.desenvolvedor_id,
    COALESCE(avg(av.nota), (0)::numeric) AS nota,
    count(av.nota) AS avaliacoes_numero,
    COALESCE(sum(hjm.horas), (0)::numeric) AS horas_jogadas
   FROM ((public.jogo jg
     FULL JOIN public.avaliacao av ON ((jg.jogo_id = av.jogo_id)))
     FULL JOIN public.horas_jogadas_mes hjm ON ((jg.jogo_id = hjm.jogo_id)))
  GROUP BY jg.jogo_id;


--
-- TOC entry 3140 (class 2618 OID 16639)
-- Name: view_usuario_assinatura _RETURN; Type: RULE; Schema: public; Owner: postgres
--

CREATE OR REPLACE VIEW public.view_usuario_assinatura AS
 SELECT usu.usuario_id,
    usu.email,
    usu.apelido,
        CASE
            WHEN (CURRENT_DATE <= (max(pa.data_pagamento) + make_interval(months => 1))) THEN 'VALIDA'::text
            ELSE 'INVALIDA'::text
        END AS assinatura_status
   FROM (public.usuario usu
     FULL JOIN public.pagamento_assinatura pa ON ((pa.usuario_id = usu.usuario_id)))
  GROUP BY usu.usuario_id;


--
-- TOC entry 2995 (class 2606 OID 16488)
-- Name: avaliacao avaliacao_jogo_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.avaliacao
    ADD CONSTRAINT avaliacao_jogo_fk FOREIGN KEY (jogo_id) REFERENCES public.jogo(jogo_id);


--
-- TOC entry 2996 (class 2606 OID 16493)
-- Name: avaliacao avaliacao_usuario_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.avaliacao
    ADD CONSTRAINT avaliacao_usuario_fk FOREIGN KEY (usuario_id) REFERENCES public.usuario(usuario_id);


--
-- TOC entry 2997 (class 2606 OID 16498)
-- Name: desenvolvedor desenvolvedor_usuario_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.desenvolvedor
    ADD CONSTRAINT desenvolvedor_usuario_fk FOREIGN KEY (usuario_id) REFERENCES public.usuario(usuario_id);


--
-- TOC entry 2998 (class 2606 OID 16503)
-- Name: ficha_aprovacao ficha_aprovacao_jogo_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ficha_aprovacao
    ADD CONSTRAINT ficha_aprovacao_jogo_fk FOREIGN KEY (jogo_id) REFERENCES public.jogo(jogo_id);


--
-- TOC entry 2999 (class 2606 OID 16508)
-- Name: horas_jogadas_mes horas_jogadas_mes_jogo_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.horas_jogadas_mes
    ADD CONSTRAINT horas_jogadas_mes_jogo_fk FOREIGN KEY (jogo_id) REFERENCES public.jogo(jogo_id);


--
-- TOC entry 3001 (class 2606 OID 16513)
-- Name: item_biblioteca item_biblioteca_jogo_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item_biblioteca
    ADD CONSTRAINT item_biblioteca_jogo_fk FOREIGN KEY (jogo_id) REFERENCES public.jogo(jogo_id);


--
-- TOC entry 3000 (class 2606 OID 16483)
-- Name: item_biblioteca item_biblioteca_usuario_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item_biblioteca
    ADD CONSTRAINT item_biblioteca_usuario_fk FOREIGN KEY (usuario_id) REFERENCES public.usuario(usuario_id);


--
-- TOC entry 3003 (class 2606 OID 16538)
-- Name: jogo jogo_desenvolvedor_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.jogo
    ADD CONSTRAINT jogo_desenvolvedor_fk FOREIGN KEY (desenvolvedor_id) REFERENCES public.desenvolvedor(desenvolvedor_id);


--
-- TOC entry 3002 (class 2606 OID 16533)
-- Name: jogo jogo_genero_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.jogo
    ADD CONSTRAINT jogo_genero_fk FOREIGN KEY (genero) REFERENCES public.genero(genero_id);


--
-- TOC entry 3004 (class 2606 OID 16478)
-- Name: pagamento_assinatura pagamento_assinatura_usuario_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pagamento_assinatura
    ADD CONSTRAINT pagamento_assinatura_usuario_fk FOREIGN KEY (usuario_id) REFERENCES public.usuario(usuario_id);


--
-- TOC entry 3008 (class 2606 OID 16577)
-- Name: pagamento_desenvolvedor pagamento_desenvolvedor_desenvolvedor; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pagamento_desenvolvedor
    ADD CONSTRAINT pagamento_desenvolvedor_desenvolvedor FOREIGN KEY (desenvolvedor_id) REFERENCES public.desenvolvedor(desenvolvedor_id);


--
-- TOC entry 3005 (class 2606 OID 16473)
-- Name: screenshot screenshot_jogo_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.screenshot
    ADD CONSTRAINT screenshot_jogo_fk FOREIGN KEY (jogo_id) REFERENCES public.jogo(jogo_id);


--
-- TOC entry 3007 (class 2606 OID 16556)
-- Name: voto voto_ficha_aprovacao; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.voto
    ADD CONSTRAINT voto_ficha_aprovacao FOREIGN KEY (ficha_aprovacao_id) REFERENCES public.ficha_aprovacao(ficha_aprovacao_id);


--
-- TOC entry 3006 (class 2606 OID 16551)
-- Name: voto voto_usuario; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.voto
    ADD CONSTRAINT voto_usuario FOREIGN KEY (usuario_id) REFERENCES public.usuario(usuario_id);


-- Completed on 2022-02-06 20:17:12

--
-- PostgreSQL database dump complete
--

