<#-- @ftlvariable name="" type="org.zoovisitor.NodeView" -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Zoo Visitor</title>

    <!-- Bootstrap -->
    <link href="/_static/css/bootstrap.min.css" rel="stylesheet">
    <style type="text/css">
        body {
          padding-top: 60px;
        }
        #children {
          padding-left: 18px;
        }
    </style>
</head>
<body>
    <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="/">Zoo Visitor</a>
            </div>
            <div class="collapse navbar-collapse">
                <ul class="nav navbar-nav pull-right">
                    <li class="active"><a href="/_about">About</a></li>
                </ul>
            </div><!--/.nav-collapse -->
        </div>
    </div>

    <div class="container">

        <div id="browser">
            <h1>Browse Zookeeper</h1>
            <p>${connection}</p>

            <ol id="path" class="breadcrumb">
              <#assign curr="" />
              <li<#if !path?has_content> class="active"</#if>><a href="/">root</a></li>
              <#if path?has_content>
                <#list path as p>
                  <#assign curr = curr + "/" + p />
                    <li<#if !p_has_next> class="active"</#if>><a href="${curr}">${p}</a></li>
                </#list>
              <#else>
                  <li></li>
              </#if>
            </ol>

            <p>${data!}</p>

            <ul id="children" class="list-unstyled32">
              <#list children as c>
                <li><a href="${curr}/${c}">${c}</a></li>
              </#list>
            </ul>

        </div>

    </div><!-- /.container -->
</body>
</html>