### O que foi feito?

### Orientações aos Revisores:

### Estou de acordo que:

1. Nomenclatura e Organização
- [ ] Nomes descritivos
<!--- Verifique se variáveis, métodos, classes, e IDs de layout têm nomes claros e autoexplicativos. Evite abreviações desnecessárias. -->
- [ ] Organização do código
<!--- Verifique se o código segue uma estrutura organizada, com métodos bem separados, lógica em classes apropriadas e uma clara separação de responsabilidades. -->

2. Tratamento de Erros e Exceções
- [ ] Tratamento de erros
<!--- Verifique se exceções são tratadas adequadamente e se o código não está deixando erros passarem silenciosamente. -->

3. Cuidado com o Ciclo de Vida do Android
- [ ] Ciclo de vida de Activity e Fragment
<!--- Certifique-se de que o código está considerando corretamente o ciclo de vida de Activity e Fragment. -->
- [ ] Operações pesadas
<!--- Não realizar operações pesadas em onCreate(). Métodos como onPause() e onDestroy() devem ser utilizados para cancelar tarefas ou listeners. -->

4. Boas Práticas de Interface de Usuário (UI)
- [ ] Consistência de Layouts
<!--- Verifique se os layouts seguem o Material Design e se as unidades de medida (como dp e sp) são usadas corretamente. -->
- [ ] Ids significativos nos componentes do layout
<!--- Os ids devem ser claros e representativos. -->
- [ ] Suporte a diferentes tamanhos de tela
<!--- Verifique se o layout é responsivo, utilizando ConstraintLayout, dimensões adaptáveis (dimens.xml), e evitando hardcoded values no layout. -->

5. Uso Adequado de Recursos (Strings, Cores, etc.)
- [ ] Strings
<!--- Verifique se os textos da interface estão em strings.xml para facilitar a internacionalização (i18n), mesmo que não tenha sido implementada ainda. -->
- [ ] Cores e dimensões
<!--- Certifique-se de que as cores e dimensões estão sendo usadas através dos arquivos de recursos (colors.xml, dimens.xml), e não hardcoded no código ou layouts. -->

6. Dependências e Bibliotecas
- [ ] Verificar dependências desatualizadas
<!--- Certifique-se de que as dependências no build.gradle estão atualizadas. -->
- [ ] Evitar bibliotecas desnecessárias
<!--- Verifique se bibliotecas que não são mais usadas foram removidas e se não há dependências desnecessárias no projeto. -->

7. Configurações do Manifest
- [ ] Configuração de orientação de tela
<!--- Verificar se no manifest todas as telas estão com configuração que não deixa deitar. -->

8. Configuração de Modo de Exibição
- [ ] Modo claro forçado
<!--- Verificar se em todas as activities do projeto estão com a configuração para forçar o modo claro. -->