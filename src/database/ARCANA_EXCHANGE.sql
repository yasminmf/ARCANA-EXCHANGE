PGDMP                      }            magic_db    17.2    17.2 +               0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false                        0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false            !           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false            "           1262    24580    magic_db    DATABASE        CREATE DATABASE magic_db WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Portuguese_Brazil.1252';
    DROP DATABASE magic_db;
                     postgres    false                        2615    24581    magic    SCHEMA        CREATE SCHEMA magic;
    DROP SCHEMA magic;
                     postgres    false            �            1255    24646    total_trocas(integer)    FUNCTION       CREATE FUNCTION magic.total_trocas(usuario_id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE total INT;
BEGIN
    SELECT COUNT(*) INTO total FROM magic.trocas
    WHERE id_usuario1 = usuario_id OR id_usuario2 = usuario_id;
    RETURN total;
END;
$$;
 6   DROP FUNCTION magic.total_trocas(usuario_id integer);
       magic               postgres    false    6            �            1255    24647    verificar_troca()    FUNCTION     �   CREATE FUNCTION magic.verificar_troca() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF NEW.id_usuario1 = NEW.id_usuario2 THEN
        RAISE EXCEPTION 'Um usuário não pode trocar cartas consigo mesmo!';
    END IF;
    RETURN NEW;
END;
$$;
 '   DROP FUNCTION magic.verificar_troca();
       magic               postgres    false    6            �            1259    24636    carta_criatura    TABLE     �   CREATE TABLE magic.carta_criatura (
    id_carta integer NOT NULL,
    poder integer NOT NULL,
    resistencia integer NOT NULL
);
 !   DROP TABLE magic.carta_criatura;
       magic         heap r       postgres    false    6            �            1259    24616    carta_troca    TABLE     �   CREATE TABLE magic.carta_troca (
    id_troca integer NOT NULL,
    id_carta integer NOT NULL,
    pertence_a integer NOT NULL
);
    DROP TABLE magic.carta_troca;
       magic         heap r       postgres    false    6            �            1259    24592    cartas    TABLE     �   CREATE TABLE magic.cartas (
    id_carta integer NOT NULL,
    nome character varying(100) NOT NULL,
    tipo character varying(50) NOT NULL,
    custo integer NOT NULL,
    id_usuario integer
);
    DROP TABLE magic.cartas;
       magic         heap r       postgres    false    6            �            1259    24591    cartas_id_carta_seq    SEQUENCE     �   CREATE SEQUENCE magic.cartas_id_carta_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE magic.cartas_id_carta_seq;
       magic               postgres    false    221    6            #           0    0    cartas_id_carta_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE magic.cartas_id_carta_seq OWNED BY magic.cartas.id_carta;
          magic               postgres    false    220            �            1259    24599    trocas    TABLE     �   CREATE TABLE magic.trocas (
    id_troca integer NOT NULL,
    id_usuario1 integer NOT NULL,
    id_usuario2 integer NOT NULL,
    data_troca timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE magic.trocas;
       magic         heap r       postgres    false    6            �            1259    24598    trocas_id_troca_seq    SEQUENCE     �   CREATE SEQUENCE magic.trocas_id_troca_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE magic.trocas_id_troca_seq;
       magic               postgres    false    6    223            $           0    0    trocas_id_troca_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE magic.trocas_id_troca_seq OWNED BY magic.trocas.id_troca;
          magic               postgres    false    222            �            1259    24583    usuarios    TABLE     �   CREATE TABLE magic.usuarios (
    id_usuario integer NOT NULL,
    nome character varying(100) NOT NULL,
    email character varying(100) NOT NULL
);
    DROP TABLE magic.usuarios;
       magic         heap r       postgres    false    6            �            1259    24582    usuarios_id_usuario_seq    SEQUENCE     �   CREATE SEQUENCE magic.usuarios_id_usuario_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE magic.usuarios_id_usuario_seq;
       magic               postgres    false    6    219            %           0    0    usuarios_id_usuario_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE magic.usuarios_id_usuario_seq OWNED BY magic.usuarios.id_usuario;
          magic               postgres    false    218            m           2604    24595    cartas id_carta    DEFAULT     p   ALTER TABLE ONLY magic.cartas ALTER COLUMN id_carta SET DEFAULT nextval('magic.cartas_id_carta_seq'::regclass);
 =   ALTER TABLE magic.cartas ALTER COLUMN id_carta DROP DEFAULT;
       magic               postgres    false    220    221    221            n           2604    24602    trocas id_troca    DEFAULT     p   ALTER TABLE ONLY magic.trocas ALTER COLUMN id_troca SET DEFAULT nextval('magic.trocas_id_troca_seq'::regclass);
 =   ALTER TABLE magic.trocas ALTER COLUMN id_troca DROP DEFAULT;
       magic               postgres    false    223    222    223            l           2604    24586    usuarios id_usuario    DEFAULT     x   ALTER TABLE ONLY magic.usuarios ALTER COLUMN id_usuario SET DEFAULT nextval('magic.usuarios_id_usuario_seq'::regclass);
 A   ALTER TABLE magic.usuarios ALTER COLUMN id_usuario DROP DEFAULT;
       magic               postgres    false    219    218    219                      0    24636    carta_criatura 
   TABLE DATA           E   COPY magic.carta_criatura (id_carta, poder, resistencia) FROM stdin;
    magic               postgres    false    225   4                 0    24616    carta_troca 
   TABLE DATA           D   COPY magic.carta_troca (id_troca, id_carta, pertence_a) FROM stdin;
    magic               postgres    false    224   t4                 0    24592    cartas 
   TABLE DATA           H   COPY magic.cartas (id_carta, nome, tipo, custo, id_usuario) FROM stdin;
    magic               postgres    false    221   �4                 0    24599    trocas 
   TABLE DATA           O   COPY magic.trocas (id_troca, id_usuario1, id_usuario2, data_troca) FROM stdin;
    magic               postgres    false    223   �9                 0    24583    usuarios 
   TABLE DATA           :   COPY magic.usuarios (id_usuario, nome, email) FROM stdin;
    magic               postgres    false    219   �9       &           0    0    cartas_id_carta_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('magic.cartas_id_carta_seq', 76, true);
          magic               postgres    false    220            '           0    0    trocas_id_troca_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('magic.trocas_id_troca_seq', 1, false);
          magic               postgres    false    222            (           0    0    usuarios_id_usuario_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('magic.usuarios_id_usuario_seq', 50, true);
          magic               postgres    false    218            {           2606    24640 "   carta_criatura carta_criatura_pkey 
   CONSTRAINT     e   ALTER TABLE ONLY magic.carta_criatura
    ADD CONSTRAINT carta_criatura_pkey PRIMARY KEY (id_carta);
 K   ALTER TABLE ONLY magic.carta_criatura DROP CONSTRAINT carta_criatura_pkey;
       magic                 postgres    false    225            y           2606    24620    carta_troca carta_troca_pkey 
   CONSTRAINT     u   ALTER TABLE ONLY magic.carta_troca
    ADD CONSTRAINT carta_troca_pkey PRIMARY KEY (id_troca, id_carta, pertence_a);
 E   ALTER TABLE ONLY magic.carta_troca DROP CONSTRAINT carta_troca_pkey;
       magic                 postgres    false    224    224    224            u           2606    24597    cartas cartas_pkey 
   CONSTRAINT     U   ALTER TABLE ONLY magic.cartas
    ADD CONSTRAINT cartas_pkey PRIMARY KEY (id_carta);
 ;   ALTER TABLE ONLY magic.cartas DROP CONSTRAINT cartas_pkey;
       magic                 postgres    false    221            w           2606    24605    trocas trocas_pkey 
   CONSTRAINT     U   ALTER TABLE ONLY magic.trocas
    ADD CONSTRAINT trocas_pkey PRIMARY KEY (id_troca);
 ;   ALTER TABLE ONLY magic.trocas DROP CONSTRAINT trocas_pkey;
       magic                 postgres    false    223            q           2606    24590    usuarios usuarios_email_key 
   CONSTRAINT     V   ALTER TABLE ONLY magic.usuarios
    ADD CONSTRAINT usuarios_email_key UNIQUE (email);
 D   ALTER TABLE ONLY magic.usuarios DROP CONSTRAINT usuarios_email_key;
       magic                 postgres    false    219            s           2606    24588    usuarios usuarios_pkey 
   CONSTRAINT     [   ALTER TABLE ONLY magic.usuarios
    ADD CONSTRAINT usuarios_pkey PRIMARY KEY (id_usuario);
 ?   ALTER TABLE ONLY magic.usuarios DROP CONSTRAINT usuarios_pkey;
       magic                 postgres    false    219            �           2620    24648    trocas trigger_verificar_troca    TRIGGER     |   CREATE TRIGGER trigger_verificar_troca BEFORE INSERT ON magic.trocas FOR EACH ROW EXECUTE FUNCTION magic.verificar_troca();
 6   DROP TRIGGER trigger_verificar_troca ON magic.trocas;
       magic               postgres    false    223    227            �           2606    24641 +   carta_criatura carta_criatura_id_carta_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY magic.carta_criatura
    ADD CONSTRAINT carta_criatura_id_carta_fkey FOREIGN KEY (id_carta) REFERENCES magic.cartas(id_carta) ON DELETE CASCADE;
 T   ALTER TABLE ONLY magic.carta_criatura DROP CONSTRAINT carta_criatura_id_carta_fkey;
       magic               postgres    false    225    4725    221                       2606    24626 %   carta_troca carta_troca_id_carta_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY magic.carta_troca
    ADD CONSTRAINT carta_troca_id_carta_fkey FOREIGN KEY (id_carta) REFERENCES magic.cartas(id_carta);
 N   ALTER TABLE ONLY magic.carta_troca DROP CONSTRAINT carta_troca_id_carta_fkey;
       magic               postgres    false    221    224    4725            �           2606    24621 %   carta_troca carta_troca_id_troca_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY magic.carta_troca
    ADD CONSTRAINT carta_troca_id_troca_fkey FOREIGN KEY (id_troca) REFERENCES magic.trocas(id_troca);
 N   ALTER TABLE ONLY magic.carta_troca DROP CONSTRAINT carta_troca_id_troca_fkey;
       magic               postgres    false    4727    224    223            �           2606    24631 '   carta_troca carta_troca_pertence_a_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY magic.carta_troca
    ADD CONSTRAINT carta_troca_pertence_a_fkey FOREIGN KEY (pertence_a) REFERENCES magic.usuarios(id_usuario);
 P   ALTER TABLE ONLY magic.carta_troca DROP CONSTRAINT carta_troca_pertence_a_fkey;
       magic               postgres    false    219    224    4723            |           2606    24649    cartas cartas_id_usuario_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY magic.cartas
    ADD CONSTRAINT cartas_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES magic.usuarios(id_usuario) ON DELETE CASCADE;
 F   ALTER TABLE ONLY magic.cartas DROP CONSTRAINT cartas_id_usuario_fkey;
       magic               postgres    false    4723    219    221            }           2606    24606    trocas trocas_id_usuario1_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY magic.trocas
    ADD CONSTRAINT trocas_id_usuario1_fkey FOREIGN KEY (id_usuario1) REFERENCES magic.usuarios(id_usuario);
 G   ALTER TABLE ONLY magic.trocas DROP CONSTRAINT trocas_id_usuario1_fkey;
       magic               postgres    false    223    219    4723            ~           2606    24611    trocas trocas_id_usuario2_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY magic.trocas
    ADD CONSTRAINT trocas_id_usuario2_fkey FOREIGN KEY (id_usuario2) REFERENCES magic.usuarios(id_usuario);
 G   ALTER TABLE ONLY magic.trocas DROP CONSTRAINT trocas_id_usuario2_fkey;
       magic               postgres    false    219    4723    223               Q   x�-��	�0��a�;�]��I�Ȇ㬁Bq��q9��)*�d"��C%Ll�H;�� �OsZs]��8]�c��(n            x������ � �         .  x�uVKr�6]7N��RC����,��$[%M�,�i��	L��b�9��E*��.7���@�3�<Z�F�l�{��]��Zox��X���۴l����LE������s���?3�$�V���Ww/e�J$t�?oZS�#W�l���	ŉH���BKw#?��ڟ��Z���G'K���{���·h��N�ue�<��(|J�ižvk��fA�bA��77����zƇьV�\>@Y����R�խ�km��*QD�5��޴Z^�2���Hf"�iY{�����u�mH���_�6�|y�m�?�E)�"R�[ŀ0|�0�7=��������,�"J�U�ŝ<um�Сo����J��O��<�,D����<�-W�``eNl�ii̈��[�;S�J�r7�����y.�}^��͟Ў�4'5��r�Cڲ)��u>�{�z���b�s:
���Λ�e_�+ �1Z�1ײt���iM��C�g/�5&�s���[s��V6|�`��[߶�6_���a�qJWڗ8Oq˅��ZA����+qla�E�!�8��w�@�r�Z8r��3ԊЇ��
+r��1�L���/y��Ǫ
�k6���	�X�剮�n�

JJX6��\�<��P	|�ҟB�OlOg��fB�X�����+��m7�$��hY�����;ny*��ׂ���! �v��V�����Z>�/Ds�(:1�v� s�7�)�mׄ�xJwF�$�p�`�9��Ѿ}��3=BI�u��5a���ij7�'9�U��.8��"�ӯ��+�e�)Ap�mm�0����9űHg��Qy\9zy�~Ls��[]\l�甤"U�;.�oC�?N8ǐS�;FΝx<�\F��M�iJo�����z_g"Ek�� ���I�󠃁�#v̈́b%2`�{���Cʲ�̦ѓ�s��Uh���㭈�2��83EWЊ���E#��"Y�i���W��v<�X�j,I'��O�n��d2����Tp�<����P�
��w�nБ��q��5�0r��ǩ��p3���C[�߱3A�)�<�M��
V�F����W�A�ǎ�(���e�?�ăE|�v�*P�(-�t��b&� �Y��Ǡ��7�q��a&v��JE���w�G���������t�79�~���-��5��a-f���멳f��Ǹ@�_�}LiyJ��Ѕ�Э��� �sF��2ڹ� ���[��#<c�_f�{�5�Q��#KqUO�A�MS��7a�@u�&ZR��#(��Ɨm���/<+U�p5U��ɺP
јN^V>{����gF�WEN���X�����H<ǭr�OxX��/B��1�            x������ � �         \  x�m�Kr�8���S�(��^���#qdǱ�̣f�"a	
( ���:�E��X 5K�����7��Fjv't-��7�ވ��i�
�j�u��^�=�D�	�醰@��cW�G[�"r�0S�t"j#���;:4n�m�L:���б�ş?�T ��6?q7H�-��ÚV��U�}s%+��P���.B���ڰ�q-�*�i�֙f���PxH[��4o�s�3���A���A.���u����=�K��̥���t�C�J�(�ȼI����V(��-е��&�"���9\���d"���ܚ��{VH��5h�.A��{��n^���%Wr���蘠�j��T��1m��)P���G��+4��M��gp��u_Kᤘ�9|P�����kM��/��\>��ʰ�B[����si���ɱ�_������J�ְ�����D̥%<�RS��d�V�K�눹t���`�6�o�z���*ƞ��#�ЏKZL�l�=4Pഁ�)��k-l��oN|\��O�$�nLC/�7_���aB%���G����`��/�c�as�n<�O�.ƌ���9�B`k��u;c��c�\\����s�hI-�:9�\JAu�,��u ���k�p������0��6P.��5��a�����H�m�\8�i�p䜢������{��fw�"I��M�|��û59�[ZL�U�\6�[��y6�+ۄ���Kx/:*w��/N�]aI���?$���T�41ڍ��=X�ü��w�r��ee�gn�=��u�\:�@����P�t�#u�z-��#\jn�e���RyT�r
ϔ9�"��6��(gԅ�4I��w=���~vv�s�     