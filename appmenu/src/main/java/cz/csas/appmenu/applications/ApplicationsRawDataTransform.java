package cz.csas.appmenu.applications;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import cz.csas.appmenu.AppItem;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.utils.csjson.CsJson;
import cz.csas.cscore.utils.csjson.JsonElement;
import cz.csas.cscore.utils.csjson.JsonObject;
import cz.csas.cscore.utils.csjson.JsonParser;
import cz.csas.cscore.utils.csjson.reflect.TypeToken;
import cz.csas.cscore.webapi.Transform;

/**
 * Webapi Transform used with {@link ApplicationListResponse} to set json raw data to each
 * {@link AppItem} entity
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 29/03/2017.
 */
class ApplicationsRawDataTransform extends Transform<ApplicationListResponse> {

    @Override
    protected ApplicationListResponse doTransform(ApplicationListResponse entity, CsSDKError error, Response response) throws CsSDKError {
        if (response != null) {
            String json = response.getBodyString();
            if (entity != null && json != null) {
                JsonElement element = new JsonParser().parse(json);
                JsonObject jsonObject = element.getAsJsonObject();
                for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                    if (jsonObject.get(entry.getKey()).isJsonArray()) {
                        List<AppItem> appItems = entity.getAppItems();
                        for (int i = 0; i < appItems.size(); i++) {
                            appItems.get(i).setRawData(getRawData(jsonObject.get(entry.getKey()).getAsJsonArray().get(i).toString()));
                        }
                    }
                }
            }
        }
        return entity;
    }

    /**
     * Converts json string to Map<key,value> raw data.
     *
     * @param json source json string
     * @return <key,value> pair Map of raw data
     */
    private Map<String, String> getRawData(String json) {
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        return new CsJson().fromJson(json, type);
    }
}
