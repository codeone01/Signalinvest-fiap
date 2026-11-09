-- Confira antes a estrutura de TB_USUARIO com 00_inspecao_schema.sql.
-- Repara somente o usuário demonstrativo e preserva os demais usuários.

UPDATE TB_USUARIO
SET DS_SENHA_HASH = 'pbkdf2_sha256$210000$FxgqNaXRFu2/y8bl05+PUg==$s1iPfApDbq7qBVqD5J1C/377FVmz+K8DNbM59REr5Ok=',
    ST_USUARIO = 'ATIVO'
WHERE LOWER(DS_EMAIL) = 'demo@signalinvest.example';

INSERT INTO TB_USUARIO (
    ID_USUARIO, NM_USUARIO, DS_EMAIL, DS_SENHA_HASH, NR_CPF,
    NR_TELEFONE, DT_CADASTRO, ST_USUARIO, TP_USUARIO
)
SELECT NOVO_ID,
       'Usuário Demonstrativo Signalinvest',
       'demo@signalinvest.example',
       'pbkdf2_sha256$210000$FxgqNaXRFu2/y8bl05+PUg==$s1iPfApDbq7qBVqD5J1C/377FVmz+K8DNbM59REr5Ok=',
       '900' || LPAD(TO_CHAR(NOVO_ID), 8, '0'),
       NULL, SYSTIMESTAMP, 'ATIVO', 'INVESTIDOR'
FROM (SELECT NVL(MAX(ID_USUARIO), 0) + 1 AS NOVO_ID FROM TB_USUARIO)
WHERE NOT EXISTS (
    SELECT 1 FROM TB_USUARIO WHERE LOWER(DS_EMAIL) = 'demo@signalinvest.example'
);

COMMIT;
