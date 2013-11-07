class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.${format})?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(redirect: '/facade/')
        "500"(view:'/error')
        "/facade/$action/$packageName/$facadeName/$methodName"(controller:'facade')
	}
}
